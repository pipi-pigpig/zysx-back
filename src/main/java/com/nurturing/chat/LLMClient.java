package com.nurturing.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nurturing.DTO.ConsultRequest;
import com.nurturing.DTO.ReportRequest;
import com.nurturing.DTO.MonitoringData;
import com.nurturing.DTO.UserProfile;
import com.nurturing.entity.ChatSentence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class LLMClient {
    private static final Logger log = LoggerFactory.getLogger(LLMClient.class);
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    @Value("${chat.service.url}")
    private String pythonServiceUrl;

    @Autowired
    private WebClient webClient;

    @Autowired
    private ObjectMapper objectMapper;

    public LLMClient(WebClient webClient, ObjectMapper objectMapper) {
        this.webClient = webClient;
        this.objectMapper = objectMapper;
    }

    public SseEmitter streamChat(String sessionId, List<ChatSentence> messages) throws IOException {
        SseEmitter emitter = new SseEmitter(180_000L);

        emitter.onCompletion(() -> log.info("SSE连接完成, sessionId: {}", sessionId));
        emitter.onTimeout(() -> log.warn("SSE连接超时, sessionId: {}", sessionId));

        log.info("开始调用Python健康咨询服务: {}, sessionId: {}", pythonServiceUrl, sessionId);

        try {
            ConsultRequest request = buildConsultRequest(sessionId, messages, null);
            String requestBody = objectMapper.writeValueAsString(request.getBody());
            log.info("发送健康咨询请求, sessionId: {}, 请求体: {}", sessionId, requestBody);

            AtomicReference<String> buffer = new AtomicReference<>("");

            webClient.post()
                    .uri(pythonServiceUrl + "/api/v1/consult")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.TEXT_EVENT_STREAM)
                    .body(BodyInserters.fromValue(request.getBody()))
                    .retrieve()
                    .bodyToFlux(DataBuffer.class)
                    .map(dataBuffer -> {
                        byte[] bytes = new byte[dataBuffer.readableByteCount()];
                        dataBuffer.read(bytes);
                        DataBufferUtils.release(dataBuffer);
                        return new String(bytes, StandardCharsets.UTF_8);
                    })
                    .concatMap(chunk -> {
                        String currentBuffer = buffer.get();
                        String newBuffer = currentBuffer + chunk;
                        buffer.set(newBuffer);
                        return processBufferedSse(newBuffer, emitter, sessionId, buffer);
                    })
                    .doOnComplete(() -> {
                        try {
                            emitter.complete();
                        } catch (Exception e) {
                            log.debug("完成SSE时出错", e);
                        }
                    })
                    .doOnError(error -> {
                        log.error("请求失败: {}", error.getMessage(), error);
                        try {
                            emitter.completeWithError(error);
                        } catch (Exception e) {
                            log.debug("完成SSE时出错", e);
                        }
                    })
                    .subscribe();

        } catch (Exception e) {
            log.error("构建请求失败: {}", e.getMessage(), e);
            try {
                emitter.completeWithError(e);
            } catch (Exception ex) {
                log.debug("完成SSE时出错", ex);
            }
        }

        return emitter;
    }

    private Flux<Void> processBufferedSse(String buffer, SseEmitter emitter, String sessionId,
            AtomicReference<String> bufferRef) {
        List<Void> results = new ArrayList<>();
        String[] lines = buffer.split("\n\n", -1);
        String remaining = "";

        for (int i = 0; i < lines.length - 1; i++) {
            String eventBlock = lines[i];
            if (!eventBlock.trim().isEmpty()) {
                try {
                    processSseEventBlock(eventBlock, emitter, sessionId);
                } catch (Exception e) {
                    log.error("处理SSE事件块失败: {}", e.getMessage());
                }
            }
        }

        remaining = lines[lines.length - 1];
        bufferRef.set(remaining);

        return Flux.fromIterable(results);
    }

    private void processSseEventBlock(String eventBlock, SseEmitter emitter, String sessionId) {
        String[] lines = eventBlock.split("\n");
        String currentEventType = null;
        String dataLine = null;

        for (String line : lines) {
            if (line.startsWith("event:")) {
                currentEventType = line.substring(6).trim();
            } else if (line.startsWith("data:")) {
                dataLine = line.substring(5).trim();
            }
        }

        if (currentEventType != null && dataLine != null) {
            try {
                Map<String, Object> data = objectMapper.readValue(dataLine, Map.class);

                switch (currentEventType) {
                    case "message":
                        handleSseMessage(data, emitter, sessionId);
                        break;
                    case "end":
                        handleSseEnd(data, emitter, sessionId);
                        break;
                    case "error":
                        handleSseError(data, emitter, sessionId);
                        break;
                    default:
                        log.debug("未知事件类型: {}", currentEventType);
                }
            } catch (Exception e) {
                log.error("解析SSE数据失败: {}, 原始数据: {}", e.getMessage(), dataLine);
            }
        }
    }

    private ConsultRequest buildConsultRequest(String sessionId, List<ChatSentence> messages, String userId) {
        String requestId = UUID.randomUUID().toString();
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMATTER);

        List<ConsultRequest.ChatMessage> chatHistory = new ArrayList<>();
        String question = "";

        if (messages != null && !messages.isEmpty()) {
            for (ChatSentence msg : messages) {
                if (msg != null && msg.getRole() != null && msg.getContent() != null) {
                    chatHistory.add(new ConsultRequest.ChatMessage(msg.getRole(), msg.getContent()));
                }
            }
            for (int i = messages.size() - 1; i >= 0; i--) {
                ChatSentence msg = messages.get(i);
                if (msg != null && "user".equals(msg.getRole()) && msg.getContent() != null
                        && !msg.getContent().isEmpty()) {
                    question = msg.getContent();
                    break;
                }
            }
        }

        if (question == null || question.isEmpty()) {
            question = "请提供健康咨询问题";
        }

        ConsultRequest.ConsultBody body = new ConsultRequest.ConsultBody(
                sessionId,
                chatHistory,
                question,
                sessionId,
                null,
                null,
                null);

        return new ConsultRequest(requestId, timestamp, userId, null, body);
    }

    private void handleSseMessage(Map<String, Object> data, SseEmitter emitter, String sessionId) {
        String content = (String) data.get("content");
        if (content != null && !content.isEmpty()) {
            try {
                Map<String, String> sseData = Map.of(
                        "sessionId", sessionId,
                        "answer", content);
                emitter.send(SseEmitter.event()
                        .name("message")
                        .data(objectMapper.writeValueAsString(sseData)));
            } catch (IOException e) {
                log.error("发送SSE消息失败: {}", e.getMessage());
            }
        }
    }

    private void handleSseEnd(Map<String, Object> data, SseEmitter emitter, String sessionId) {
        log.info("SSE流结束, sessionId: {}, 元数据: {}", sessionId, data);
        try {
            Map<String, Object> sseData = Map.of(
                    "sessionId", sessionId,
                    "type", "end",
                    "metadata", data);
            emitter.send(SseEmitter.event()
                    .name("end")
                    .data(objectMapper.writeValueAsString(sseData)));
        } catch (IOException e) {
            log.error("发送SSE结束事件失败: {}", e.getMessage());
        }
    }

    private void handleSseError(Map<String, Object> data, SseEmitter emitter, String sessionId) {
        Integer errorCode = (Integer) data.get("error_code");
        String errorMessage = (String) data.get("error_message");
        log.error("SSE错误事件, sessionId: {}, 错误码: {}, 错误信息: {}", sessionId, errorCode, errorMessage);

        try {
            Map<String, Object> sseData = Map.of(
                    "sessionId", sessionId,
                    "type", "error",
                    "error_code", errorCode != null ? errorCode : -1,
                    "error_message", errorMessage != null ? errorMessage : "未知错误");
            emitter.send(SseEmitter.event()
                    .name("error")
                    .data(objectMapper.writeValueAsString(sseData)));
        } catch (IOException e) {
            log.error("发送SSE错误事件失败: {}", e.getMessage());
        }
    }

    public SseEmitter streamGenerateHealthReport(String sessionId, List<ChatSentence> messages) throws IOException {
        return streamGenerateHealthReport(sessionId, messages, null, null, null);
    }

    public SseEmitter streamGenerateHealthReport(String sessionId, List<ChatSentence> messages,
            MonitoringData monitoringData, UserProfile userProfile, String userId) throws IOException {
        SseEmitter emitter = new SseEmitter(300_000L);

        emitter.onCompletion(() -> log.info("健康报告SSE连接完成, sessionId: {}", sessionId));
        emitter.onTimeout(() -> log.warn("健康报告SSE连接超时, sessionId: {}", sessionId));

        log.info("开始调用Python健康报告服务: {}, sessionId: {}", pythonServiceUrl, sessionId);

        try {
            ReportRequest request = buildReportRequest(sessionId, monitoringData, userProfile, userId);
            String requestBody = objectMapper.writeValueAsString(request.getBody());
            log.info("发送健康报告请求, sessionId: {}, 请求体: {}", sessionId, requestBody);

            AtomicReference<String> buffer = new AtomicReference<>("");

            webClient.post()
                    .uri(pythonServiceUrl + "/api/v1/report")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.TEXT_EVENT_STREAM)
                    .body(BodyInserters.fromValue(request.getBody()))
                    .retrieve()
                    .bodyToFlux(DataBuffer.class)
                    .map(dataBuffer -> {
                        byte[] bytes = new byte[dataBuffer.readableByteCount()];
                        dataBuffer.read(bytes);
                        DataBufferUtils.release(dataBuffer);
                        return new String(bytes, StandardCharsets.UTF_8);
                    })
                    .concatMap(chunk -> {
                        String currentBuffer = buffer.get();
                        String newBuffer = currentBuffer + chunk;
                        buffer.set(newBuffer);
                        return processBufferedSse(newBuffer, emitter, sessionId, buffer);
                    })
                    .doOnComplete(() -> {
                        try {
                            emitter.complete();
                        } catch (Exception e) {
                            log.debug("完成SSE时出错", e);
                        }
                    })
                    .doOnError(error -> {
                        log.error("请求失败: {}", error.getMessage(), error);
                        try {
                            emitter.completeWithError(error);
                        } catch (Exception e) {
                            log.debug("完成SSE时出错", e);
                        }
                    })
                    .subscribe();

        } catch (Exception e) {
            log.error("构建请求失败: {}", e.getMessage(), e);
            try {
                emitter.completeWithError(e);
            } catch (Exception ex) {
                log.debug("完成SSE时出错", ex);
            }
        }

        return emitter;
    }

    private ReportRequest buildReportRequest(String sessionId, MonitoringData monitoringData, UserProfile userProfile,
            String userId) {
        String requestId = UUID.randomUUID().toString();
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMATTER);

        if (monitoringData == null) {
            monitoringData = new MonitoringData();
        }
        if (userProfile == null) {
            userProfile = new UserProfile();
        }

        ReportRequest.ReportBody body = new ReportRequest.ReportBody(
                sessionId,
                monitoringData,
                userProfile,
                sessionId);

        return new ReportRequest(requestId, timestamp, userId, null, body);
    }
}

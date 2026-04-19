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
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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

            webClient.post()
                    .uri(pythonServiceUrl + "/api/v1/consult")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.TEXT_EVENT_STREAM)
                    .body(BodyInserters.fromValue(request))
                    .exchangeToMono(clientResponse -> {
                        log.debug("响应状态码: {}", clientResponse.statusCode());

                        if (clientResponse.statusCode().is2xxSuccessful()) {
                            return clientResponse.bodyToMono(String.class);
                        } else {
                            return clientResponse.bodyToMono(String.class)
                                    .flatMap(errorBody -> {
                                        log.error("HTTP错误响应: {}", errorBody);
                                        return Mono
                                                .error(new RuntimeException("HTTP错误: " + clientResponse.statusCode()));
                                    });
                        }
                    })
                    .subscribe(
                            responseBody -> processSseResponse(responseBody, emitter, sessionId),
                            error -> {
                                log.error("请求失败: {}", error.getMessage(), error);
                                try {
                                    emitter.completeWithError(error);
                                } catch (Exception e) {
                                    log.debug("完成SSE时出错", e);
                                }
                            });

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

    private ConsultRequest buildConsultRequest(String sessionId, List<ChatSentence> messages, String userId) {
        String requestId = UUID.randomUUID().toString();
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMATTER);

        List<ConsultRequest.ChatMessage> chatHistory = new ArrayList<>();
        String question = "";

        if (messages != null && !messages.isEmpty()) {
            for (ChatSentence msg : messages) {
                chatHistory.add(new ConsultRequest.ChatMessage(msg.getRole(), msg.getContent()));
            }
            for (int i = messages.size() - 1; i >= 0; i--) {
                if ("user".equals(messages.get(i).getRole())) {
                    question = messages.get(i).getContent();
                    break;
                }
            }
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

    private void processSseResponse(String responseBody, SseEmitter emitter, String sessionId) {
        String[] lines = responseBody.split("\n");
        String currentEventType = null;

        for (String line : lines) {
            if (line.trim().isEmpty()) {
                continue;
            }

            if (line.startsWith("event:")) {
                currentEventType = line.substring(6).trim();
                continue;
            }

            if (line.startsWith("data:") && currentEventType != null) {
                String jsonStr = line.substring(5).trim();

                try {
                    Map<String, Object> data = objectMapper.readValue(jsonStr, Map.class);

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
                    log.error("解析SSE数据失败: {}, 原始数据: {}", e.getMessage(), jsonStr);
                }
            }
        }

        try {
            emitter.complete();
        } catch (Exception e) {
            log.debug("完成SSE时出错", e);
        }
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

            webClient.post()
                    .uri(pythonServiceUrl + "/api/v1/report")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.TEXT_EVENT_STREAM)
                    .body(BodyInserters.fromValue(request))
                    .exchangeToMono(clientResponse -> {
                        log.debug("响应状态码: {}", clientResponse.statusCode());

                        if (clientResponse.statusCode().is2xxSuccessful()) {
                            return clientResponse.bodyToMono(String.class);
                        } else {
                            return clientResponse.bodyToMono(String.class)
                                    .flatMap(errorBody -> {
                                        log.error("HTTP错误响应: {}", errorBody);
                                        return Mono
                                                .error(new RuntimeException("HTTP错误: " + clientResponse.statusCode()));
                                    });
                        }
                    })
                    .subscribe(
                            responseBody -> processSseResponse(responseBody, emitter, sessionId),
                            error -> {
                                log.error("请求失败: {}", error.getMessage(), error);
                                try {
                                    emitter.completeWithError(error);
                                } catch (Exception e) {
                                    log.debug("完成SSE时出错", e);
                                }
                            });

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

        ReportRequest.ReportBody body = new ReportRequest.ReportBody(
                sessionId,
                monitoringData,
                userProfile,
                sessionId);

        return new ReportRequest(requestId, timestamp, userId, null, body);
    }
}

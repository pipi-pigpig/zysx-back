package com.nurturing.chat;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nurturing.DTO.ChatModelRequest;
import com.nurturing.entity.ChatSentence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Component
public class LLMClient {
    @Value("${chat.service.url}")
    private String pythonServiceUrl;

    @Autowired
    private WebClient webClient;

    @Autowired
    private ObjectMapper objectMapper; // 用于 JSON 序列化/反序列化


    public LLMClient(WebClient webClient, ObjectMapper objectMapper) {
        this.webClient = webClient;
        this.objectMapper = objectMapper;
    }

    public SseEmitter streamChat(String sessionId, List<ChatSentence> messages) throws IOException {
        SseEmitter emitter = new SseEmitter(180_000L);

        // 设置SSE头信息
        emitter.onCompletion(() -> {
            System.out.println("SSE连接完成");
        });

        emitter.onTimeout(() -> {
            System.out.println("SSE连接超时");
        });

        System.out.println("=== DEBUG START ===");
        System.out.println("1. 开始调用Python服务: " + pythonServiceUrl);

        try {
            // 使用exchangeToMono获取完整响应
            webClient.post()
                    .uri(pythonServiceUrl+"/v1/medical_rag_stream")
//                    .uri("http://[2409:8938:8e:f5b:b7e2:382c:9960:de43]:8001/v1/medical_rag_stream")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("X-Session-ID", sessionId)
                    .header("X-Timestamp", String.valueOf(System.currentTimeMillis() / 1000))
                    .accept(MediaType.TEXT_EVENT_STREAM)
                    .body(BodyInserters.fromValue(new ChatModelRequest(messages)))
                    .exchangeToMono(clientResponse -> {
                        System.out.println("2. 响应状态码: " + clientResponse.statusCode());
//                        System.out.println("3. 响应头: " + clientResponse.headers().asHttpHeaders());

                        if (clientResponse.statusCode().is2xxSuccessful()) {
                            // 将响应体作为字符串读取
                            return clientResponse.bodyToMono(String.class);
                        } else {
                            return clientResponse.bodyToMono(String.class)
                                    .flatMap(errorBody -> {
                                        System.out.println("4. 错误响应: " + errorBody);
                                        return Mono.error(new RuntimeException("HTTP错误: " + clientResponse.statusCode()));
                                    });
                        }
                    })
                    .subscribe(
                            responseBody -> {
//                                System.out.println("5. 收到完整响应体");
//                                System.out.println("6. 响应体长度: " + responseBody.length());
//                                System.out.println("7. 响应体前500字符: " +
//                                        (responseBody.length() > 500 ? responseBody.substring(0, 500) + "..." : responseBody));

                                // 按行处理响应体
                                String[] lines = responseBody.split("\n");
//                                System.out.println("8. 总行数: " + lines.length);

                                for (int i = 0; i < lines.length; i++) {
                                    String line = lines[i];
                                    if (line.trim().isEmpty()) {
//                                        System.out.println("9." + i + ": 空行，跳过");
                                        continue;
                                    }

//                                    System.out.println("10." + i + ": 处理行: " + line);

                                    // 检查是否以空格开头
                                    if (line.startsWith(" ")) {
                                        String jsonStr = line.substring(1).trim();
//                                        System.out.println("11." + i + ": 提取的JSON字符串: " + jsonStr);

                                        if ("[DONE]".equals(jsonStr)) {
//                                            System.out.println("12. 收到[DONE]信号");
                                            try {
                                                emitter.complete();
                                            } catch (Exception e) {
                                                // 忽略
                                            }
                                            break;
                                        }

                                        try {
                                            // 解析JSON
                                            Map<String, Object> jsonData = objectMapper.readValue(jsonStr, Map.class);
//                                            System.out.println("13." + i + ": 解析的JSON: " + jsonData);

                                            // 提取内容
                                            List<Map<String, Object>> choices = (List<Map<String, Object>>) jsonData.get("choices");
                                            if (choices != null && !choices.isEmpty()) {
                                                Map<String, Object> firstChoice = choices.get(0);
                                                Map<String, Object> delta = (Map<String, Object>) firstChoice.get("delta");

                                                if (delta != null) {
                                                    String content = (String) delta.get("content");
//                                                    System.out.println("14." + i + ": 提取的内容: " + content);

                                                    if (content != null && !content.isEmpty()) {
                                                        // 发送给前端
                                                        Map<String, String> sseData = Map.of(
                                                                "sessionId", sessionId,
                                                                "answer", content
                                                        );

                                                        try {
                                                            emitter.send(SseEmitter.event()
                                                                    .data(objectMapper.writeValueAsString(sseData)));
//                                                            System.out.println("15." + i + ": 发送成功");
                                                        } catch (IOException e) {
                                                            System.out.println("16." + i + ": 发送失败: " + e.getMessage());
                                                        }
                                                    }
                                                }
                                            }
                                        } catch (Exception e) {
                                            System.out.println("17." + i + ": JSON解析失败: " + e.getMessage());
                                            System.out.println("18." + i + ": 原始JSON: " + jsonStr);
                                        }
                                    } else {
//                                        System.out.println("19." + i + ": 不以空格开头，跳过");
                                    }
                                }

                                System.out.println("20. 所有行处理完成");
                                try {
                                    emitter.complete();
                                } catch (Exception e) {
                                    // 忽略
                                }
                            },
                            error -> {
                                System.out.println("21. 请求失败: " + error.getMessage());
                                error.printStackTrace();
                                try {
                                    emitter.completeWithError(error);
                                } catch (Exception e) {
                                    // 忽略
                                }
                            }
                    );

        } catch (Exception e) {
            System.out.println("22. 构建请求失败: " + e.getMessage());
            e.printStackTrace();
            try {
                emitter.completeWithError(e);
            } catch (Exception ex) {
                // 忽略
            }
        }

        System.out.println("=== DEBUG END ===");

        return emitter;
    }


    public SseEmitter streamGenerateHealthReport(String sessionId, List<ChatSentence> messages) throws IOException {
        SseEmitter emitter = new SseEmitter(180_000L);

        // 设置SSE头信息
        emitter.onCompletion(() -> {
            System.out.println("SSE连接完成");
        });

        emitter.onTimeout(() -> {
            System.out.println("SSE连接超时");
        });

        System.out.println("=== DEBUG START ===");
        System.out.println("1. 开始调用Python服务: " + pythonServiceUrl);

        try {
            // 使用exchangeToMono获取完整响应
            webClient.post()
                    .uri(pythonServiceUrl+"/v1/health_report")
//                    .uri("http://[2409:8938:8e:f5b:b7e2:382c:9960:de43]:8001/v1/medical_rag_stream")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("X-Session-ID", sessionId)
                    .header("X-Timestamp", String.valueOf(System.currentTimeMillis() / 1000))
                    .accept(MediaType.TEXT_EVENT_STREAM)
                    .body(BodyInserters.fromValue(new ChatModelRequest(messages)))
                    .exchangeToMono(clientResponse -> {
                        System.out.println("2. 响应状态码: " + clientResponse.statusCode());
//                        System.out.println("3. 响应头: " + clientResponse.headers().asHttpHeaders());

                        if (clientResponse.statusCode().is2xxSuccessful()) {
                            // 将响应体作为字符串读取
                            return clientResponse.bodyToMono(String.class);
                        } else {
                            return clientResponse.bodyToMono(String.class)
                                    .flatMap(errorBody -> {
                                        System.out.println("4. 错误响应: " + errorBody);
                                        return Mono.error(new RuntimeException("HTTP错误: " + clientResponse.statusCode()));
                                    });
                        }
                    })
                    .subscribe(
                            responseBody -> {
//                                System.out.println("5. 收到完整响应体");
//                                System.out.println("6. 响应体长度: " + responseBody.length());
//                                System.out.println("7. 响应体前500字符: " +
//                                        (responseBody.length() > 500 ? responseBody.substring(0, 500) + "..." : responseBody));

                                // 按行处理响应体
                                String[] lines = responseBody.split("\n");
//                                System.out.println("8. 总行数: " + lines.length);

                                for (int i = 0; i < lines.length; i++) {
                                    String line = lines[i];
                                    if (line.trim().isEmpty()) {
//                                        System.out.println("9." + i + ": 空行，跳过");
                                        continue;
                                    }

//                                    System.out.println("10." + i + ": 处理行: " + line);

                                    // 检查是否以空格开头
                                    if (line.startsWith(" ")) {
                                        String jsonStr = line.substring(1).trim();
//                                        System.out.println("11." + i + ": 提取的JSON字符串: " + jsonStr);

                                        if ("[DONE]".equals(jsonStr)) {
//                                            System.out.println("12. 收到[DONE]信号");
                                            try {
                                                emitter.complete();
                                            } catch (Exception e) {
                                                // 忽略
                                            }
                                            break;
                                        }

                                        try {
                                            // 解析JSON
                                            Map<String, Object> jsonData = objectMapper.readValue(jsonStr, Map.class);
//                                            System.out.println("13." + i + ": 解析的JSON: " + jsonData);

                                            // 提取内容
                                            List<Map<String, Object>> choices = (List<Map<String, Object>>) jsonData.get("choices");
                                            if (choices != null && !choices.isEmpty()) {
                                                Map<String, Object> firstChoice = choices.get(0);
                                                Map<String, Object> delta = (Map<String, Object>) firstChoice.get("delta");

                                                if (delta != null) {
                                                    String content = (String) delta.get("content");
//                                                    System.out.println("14." + i + ": 提取的内容: " + content);

                                                    if (content != null && !content.isEmpty()) {
                                                        // 发送给前端
                                                        Map<String, String> sseData = Map.of(
                                                                "sessionId", sessionId,
                                                                "answer", content
                                                        );

                                                        try {
                                                            emitter.send(SseEmitter.event()
                                                                    .data(objectMapper.writeValueAsString(sseData)));
//                                                            System.out.println("15." + i + ": 发送成功");
                                                        } catch (IOException e) {
                                                            System.out.println("16." + i + ": 发送失败: " + e.getMessage());
                                                        }
                                                    }
                                                }
                                            }
                                        } catch (Exception e) {
                                            System.out.println("17." + i + ": JSON解析失败: " + e.getMessage());
                                            System.out.println("18." + i + ": 原始JSON: " + jsonStr);
                                        }
                                    } else {
//                                        System.out.println("19." + i + ": 不以空格开头，跳过");
                                    }
                                }

                                System.out.println("20. 所有行处理完成");
                                try {
                                    emitter.complete();
                                } catch (Exception e) {
                                    // 忽略
                                }
                            },
                            error -> {
                                System.out.println("21. 请求失败: " + error.getMessage());
                                error.printStackTrace();
                                try {
                                    emitter.completeWithError(error);
                                } catch (Exception e) {
                                    // 忽略
                                }
                            }
                    );

        } catch (Exception e) {
            System.out.println("22. 构建请求失败: " + e.getMessage());
            e.printStackTrace();
            try {
                emitter.completeWithError(e);
            } catch (Exception ex) {
                // 忽略
            }
        }

        System.out.println("=== DEBUG END ===");

        return emitter;
    }
}

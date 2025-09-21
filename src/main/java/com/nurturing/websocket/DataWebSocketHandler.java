// com/nurturing/websocket/DataWebSocketHandler.java
package com.nurturing.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nurturing.Service.HeartDataService;
import com.nurturing.Service.OxygenDataService;
import com.nurturing.Service.PiDataService;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.HashMap;
import java.util.Map;

@Component
@ServerEndpoint("/ws/data")
@Slf4j
public class DataWebSocketHandler {

    // 注意：WebSocket 中不能直接注入 Service，需要通过 ApplicationContext 获取
    private static HeartDataService heartDataService;
    private static OxygenDataService oxygenDataService;
    private static PiDataService piDataService;
    private static ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    public void setServices(
            HeartDataService heartDataService,
            OxygenDataService oxygenDataService,
            PiDataService piDataService) {
        DataWebSocketHandler.heartDataService = heartDataService;
        DataWebSocketHandler.oxygenDataService = oxygenDataService;
        DataWebSocketHandler.piDataService = piDataService;
    }

    @OnOpen
    public void onOpen(Session session) {
        log.info("WebSocket 连接已建立: {}", session.getId());
    }

    @OnMessage
    public void onMessage(String message, Session session) throws Exception {
        log.info("收到消息: {}", message);

        Map<String, Object> request = objectMapper.readValue(message, Map.class);
        String type = (String) request.get("type");
        Long userId = ((Number) request.get("user_id")).longValue();

        Object result = null;

        switch (type) {
            case "heart":
                result = heartDataService.getById(userId);
                break;
            case "oxygen":
                result = oxygenDataService.getById(userId);
                break;
            case "pi":
                result = piDataService.getById(userId);
                break;
            default:
                result = new HashMap<String, String>() {{
                    put("error", "未知的数据类型");
                }};
        }

        session.getBasicRemote().sendText(objectMapper.writeValueAsString(result));
    }

    @OnClose
    public void onClose(Session session) {
        log.info("WebSocket 连接关闭: {}", session.getId());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.error("WebSocket 异常", throwable);
    }
}
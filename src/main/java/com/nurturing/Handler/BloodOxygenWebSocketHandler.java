package com.nurturing.Handler;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.nurturing.Service.BloodOxygenService;
import com.nurturing.entity.BloodOxygen;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;


/**
 * @author ZhangQinAn
 * @email 242646968@qq.com
 * @since 2025/10/22 上午8:49
 */
@Component
@Slf4j
public class BloodOxygenWebSocketHandler extends TextWebSocketHandler {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BloodOxygenService bloodOxygenService;

    // 存储连接的客户端
    private static final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    // 存储每个连接的定时任务
    private static final Map<String, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    // 定时任务线程池
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String userId = getUserIdFromSession(session);
        sessions.put(userId, session);
        log.info("WebSocket连接建立，用户ID: {}, 当前连接数: {}", userId, sessions.size());

        // 启动定时推送任务
        startDataPushTask(session, userId);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String userId = getUserIdFromSession(session);
        sessions.remove(userId);

        // 取消定时任务
        ScheduledFuture<?> task = scheduledTasks.remove(userId);
        if (task != null && !task.isCancelled()) {
            task.cancel(false);
        }

        log.info("WebSocket连接关闭，用户ID: {}, 状态: {}", userId, status);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("收到客户端消息: {}", payload);

        if ("ping".equals(payload)) {
            session.sendMessage(new TextMessage("pong"));
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        String userId = getUserIdFromSession(session);
        log.error("WebSocket传输错误，用户ID: {}", userId, exception);

        // 取消定时任务
        ScheduledFuture<?> task = scheduledTasks.remove(userId);
        if (task != null && !task.isCancelled()) {
            task.cancel(false);
        }

        sessions.remove(userId);
    }

    private String getUserIdFromSession(WebSocketSession session) {
        URI uri = session.getUri();
        String query = uri.getQuery();
        if (query != null && query.contains("userId=")) {
            return query.split("userId=")[1].split("&")[0];
        }
        return session.getId();
    }

    private void startDataPushTask(WebSocketSession session, String userId) {
        // 每5秒推送一次数据
        ScheduledFuture<?> task = scheduler.scheduleAtFixedRate(() -> {
            try {
                if (session.isOpen()) {
                    // 获取最新的血氧数据（例如最近10条记录）
                    List<BloodOxygen> data = bloodOxygenService.getRecentData(Long.valueOf(userId));
                    if (!data.isEmpty()) {
                        String jsonData = objectMapper.writeValueAsString(data);
                        session.sendMessage(new TextMessage(jsonData));
                        log.info("推送血氧数据给用户: {}, 数据条数: {}", userId, data.size());
                    } else {
                        // 如果没有数据，可以发送一个空数组或状态消息
                        session.sendMessage(new TextMessage("[]"));
                    }
                } else {
                    // 如果连接已关闭，取消任务
                    ScheduledFuture<?> existingTask = scheduledTasks.get(userId);
                    if (existingTask != null) {
                        existingTask.cancel(false);
                        scheduledTasks.remove(userId);
                    }
                }
            } catch (Exception e) {
                log.error("推送数据失败，用户ID: {}", userId, e);
                try {
                    session.close();
                } catch (IOException ioException) {
                    log.error("关闭session失败", ioException);
                }

                // 移除session和任务
                sessions.remove(userId);
                ScheduledFuture<?> existingTask = scheduledTasks.remove(userId);
                if (existingTask != null) {
                    existingTask.cancel(false);
                }
            }
        }, 0, 5, TimeUnit.SECONDS); // 立即开始，每5秒执行一次

        // 保存任务引用以便后续取消
        scheduledTasks.put(userId, task);
    }

    // 向指定用户推送数据
    public void pushDataToUser(String userId, List<BloodOxygen> data) {
        WebSocketSession session = sessions.get(userId);
        if (session != null && session.isOpen()) {
            try {
                String jsonData = objectMapper.writeValueAsString(data);
                session.sendMessage(new TextMessage(jsonData));
            } catch (Exception e) {
                log.error("推送数据失败", e);
            }
        }
    }

    // 向所有连接的用户推送数据
    public void pushDataToAll(List<BloodOxygen> data) {
        try {
            String jsonData = objectMapper.writeValueAsString(data);
            for (WebSocketSession session : sessions.values()) {
                try {
                    if (session.isOpen()) {
                        session.sendMessage(new TextMessage(jsonData));
                    }
                } catch (Exception e) {
                    log.error("推送数据失败", e);
                }
            }
        } catch (Exception e) {
            log.error("序列化数据失败", e);
        }
    }
}
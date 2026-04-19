package com.nurturing.Handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nurturing.Service.HealthData;
import com.nurturing.message.HealthDataMessage;
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
 * @since 2025/10/22 上午10:33
 */
@Component
@Slf4j
public abstract class BaseHealthDataWebSocketHandler<T extends HealthData> extends TextWebSocketHandler {

    @Autowired
    protected ObjectMapper objectMapper;

    protected final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    protected final Map<String, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();
    protected final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(10);

    // 抽象方法，子类需要实现具体的数据获取逻辑
    protected abstract List<T> getRecentData(Long userId);
    protected abstract String getDataType();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String userId = getUserIdFromSession(session);
        if (userId == null) {
            log.warn("WebSocket连接拒绝：userId无效，数据类型: {}, session: {}", getDataType(), session.getId());
            session.close(CloseStatus.BAD_DATA);
            return;
        }
        sessions.put(userId, session);
        log.info("WebSocket连接建立，用户ID: {}, 数据类型: {}, 当前连接数: {}",
                userId, getDataType(), sessions.size());

        startDataPushTask(session, userId);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String userId = getUserIdFromSession(session);
        if (userId != null) {
            sessions.remove(userId);
            ScheduledFuture<?> task = scheduledTasks.remove(userId);
            if (task != null && !task.isCancelled()) {
                task.cancel(false);
            }
        }
        log.info("WebSocket连接关闭，用户ID: {}, 数据类型: {}, 状态: {}",
                userId, getDataType(), status);
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
        log.error("WebSocket传输错误，用户ID: {}, 数据类型: {}", userId, getDataType(), exception);
        if (userId != null) {
            ScheduledFuture<?> task = scheduledTasks.remove(userId);
            if (task != null && !task.isCancelled()) {
                task.cancel(false);
            }
            sessions.remove(userId);
        }
    }

    private String getUserIdFromSession(WebSocketSession session) {
        URI uri = session.getUri();
        String query = uri.getQuery();
        if (query != null && query.contains("userId=")) {
            String userId = query.split("userId=")[1].split("&")[0];
            if (userId == null || userId.isEmpty() || "null".equalsIgnoreCase(userId)) {
                return null;
            }
            try {
                Long.valueOf(userId);
                return userId;
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    private void startDataPushTask(WebSocketSession session, String userId) {
        ScheduledFuture<?> task = scheduler.scheduleAtFixedRate(() -> {
            try {
                if (session.isOpen()) {
                    List<T> data = getRecentData(Long.valueOf(userId));
                    if (!data.isEmpty()) {
                        HealthDataMessage<T> message = new HealthDataMessage<>(getDataType(), data);
                        String jsonData = objectMapper.writeValueAsString(message);
                        log.info("准备推送数据 - 类型: {}, 用户: {}, 数据: {}", getDataType(), userId, jsonData);
                        session.sendMessage(new TextMessage(jsonData));
                        log.info("推送{}数据给用户: {}, 数据条数: {}",
                                getDataType(), userId, data.size());
                    } else {
                        session.sendMessage(new TextMessage("[]"));
                        log.info("发送空数组给用户: {}", userId);
                    }
                } else {
                    ScheduledFuture<?> existingTask = scheduledTasks.get(userId);
                    if (existingTask != null) {
                        existingTask.cancel(false);
                        scheduledTasks.remove(userId);
                    }
                }
            } catch (Exception e) {
                log.error("推送{}数据失败，用户ID: {}", getDataType(), userId, e);
                try {
                    session.close();
                } catch (IOException ioException) {
                    log.error("关闭session失败", ioException);
                }

                sessions.remove(userId);
                ScheduledFuture<?> existingTask = scheduledTasks.remove(userId);
                if (existingTask != null) {
                    existingTask.cancel(false);
                }
            }
        }, 0, 5, TimeUnit.SECONDS);

        scheduledTasks.put(userId, task);
    }

    public void pushDataToUser(String userId, List<T> data) {
        WebSocketSession session = sessions.get(userId);
        if (session != null && session.isOpen()) {
            try {
                HealthDataMessage<T> message = new HealthDataMessage<>(getDataType(), data);
                String jsonData = objectMapper.writeValueAsString(message);
                session.sendMessage(new TextMessage(jsonData));
            } catch (Exception e) {
                log.error("推送{}数据失败", getDataType(), e);
            }
        }
    }
}

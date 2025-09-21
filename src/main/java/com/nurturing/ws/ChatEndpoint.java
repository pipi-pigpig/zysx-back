//package com.nurturing.ws;
//
//import com.nurturing.Service.impI.HealthDataService;
//import com.nurturing.entity.HealthData;
//import jakarta.annotation.PostConstruct;
//import jakarta.annotation.PreDestroy;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.eclipse.paho.client.mqttv3.*;
//import org.json.JSONObject;
//import org.springframework.stereotype.Component;
//
//import java.time.Instant;
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//
//@Component
//@RequiredArgsConstructor
//@Slf4j
//public class ChatEndpoint {
//    private MqttClient mqttClient;
//    private final HealthDataService dataService;
//    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
//    private volatile boolean mqttConnected = false;
//
//    @PostConstruct
//    public void init() {
//        connectToMqtt();
//    }
//
//    private void connectToMqtt() {
//        try {
//            if (mqttClient != null && mqttClient.isConnected()) {
//                mqttClient.disconnect();
//            }
//
//            mqttClient = new MqttClient("tcp://localhost:1888", "server-client-" + System.currentTimeMillis());
//
//            MqttConnectOptions options = new MqttConnectOptions();
//            options.setCleanSession(true);
//            options.setAutomaticReconnect(true);
//            options.setConnectionTimeout(30);
//            options.setKeepAliveInterval(60);
//
//            mqttClient.connect(options);
//            mqttClient.subscribe("t/pub", 1);
//            mqttClient.setCallback(new MqttCallback() {
//                @Override
//                public void connectionLost(Throwable throwable) {
//                    log.warn("MQTT connection lost: {}", throwable.getMessage());
//                    mqttConnected = false;
//                    // 启动重连机制
//                    scheduler.schedule(ChatEndpoint.this::reconnect, 5, TimeUnit.SECONDS);
//                }
//
//                @Override
//                public void messageArrived(String topic, MqttMessage message) {
//                    try {
//                        processMessage(new String(message.getPayload()));
//                    } catch (Exception e) {
//                        log.error("Error processing MQTT message: {}", e.getMessage(), e);
//                    }
//                }
//
//                @Override
//                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
//                    // 不需要实现
//                }
//            });
//
//            mqttConnected = true;
//            log.info("MQTT connected successfully");
//
//        } catch (Exception e) {
//            log.error("MQTT connection failed: {}", e.getMessage(), e);
//            mqttConnected = false;
//            // 5秒后重试
//            scheduler.schedule(this::connectToMqtt, 5, TimeUnit.SECONDS);
//        }
//    }
//
//    private void reconnect() {
//        if (!mqttConnected) {
//            log.info("Attempting to reconnect to MQTT...");
//            connectToMqtt();
//        }
//    }
//
//    private void processMessage(String payload) {
//        try {
//            log.debug("Received MQTT message: {}", payload);
//            JSONObject json = new JSONObject(payload);
//            JSONObject data = json.getJSONObject("data");
//            JSONObject values = data.getJSONObject("value");
//
//            HealthData healthData = new HealthData();
//            healthData.setSpo2(values.getInt("spo2"));
//            healthData.setBmp(values.getInt("bmp"));
//            healthData.setPi(values.getInt("pi"));
//            healthData.setDeviceCode(json.getString("deviceCode"));
//            healthData.setMac(data.getString("mac"));
//            healthData.setRecordTime(convertTimestamp(json.getLong("time")));
//            healthData.setUser_id(1);
//
//            dataService.addToBuffer(healthData);
//            log.debug("Health data added to buffer: {}", healthData);
//
//        } catch (Exception e) {
//            log.error("Error processing message: {}", e.getMessage(), e);
//            log.error("Problematic payload: {}", payload);
//        }
//    }
//
//    private LocalDateTime convertTimestamp(long timestamp) {
//        return Instant.ofEpochSecond(timestamp)
//                .atZone(ZoneId.systemDefault())
//                .toLocalDateTime();
//    }
//
//    @PreDestroy
//    public void cleanup() {
//        try {
//            if (mqttClient != null && mqttClient.isConnected()) {
//                mqttClient.disconnect();
//            }
//            scheduler.shutdown();
//        } catch (Exception e) {
//            log.error("Error during cleanup: {}", e.getMessage());
//        }
//    }
//
//    // 提供连接状态检查方法
//    public boolean isMqttConnected() {
//        return mqttClient != null && mqttClient.isConnected();
//    }
//}
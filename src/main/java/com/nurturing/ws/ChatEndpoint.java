//package com.nurturing.ws;
//
//import com.nurturing.Service.DataService;
////import com.nurturing.Service.impI.HealthDataService;
//import com.nurturing.Service.impI.HealthDataService;
//import com.nurturing.entity.HealthData;
//import jakarta.annotation.PostConstruct;
//import jakarta.websocket.OnClose;
//import jakarta.websocket.OnMessage;
//import jakarta.websocket.OnOpen;
//import jakarta.websocket.Session;
//import jakarta.websocket.server.ServerEndpoint;
//import lombok.RequiredArgsConstructor;
//import org.eclipse.paho.client.mqttv3.*;
//import org.json.JSONObject;
//import org.springframework.stereotype.Component;
//
//import java.text.SimpleDateFormat;
//import java.time.Instant;
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.util.Date;
//
//@ServerEndpoint("/chat")
//@Component
//@RequiredArgsConstructor
//public class ChatEndpoint {
//    private static MqttClient mqttClient;
//    private final HealthDataService dataService;
//
//    @PostConstruct
//    public void init() throws MqttException {
//        mqttClient = new MqttClient("tcp://localhost:1888", "server-client");
//        mqttClient.connect();
//        mqttClient.subscribe("t/pub", 1);
//        mqttClient.setCallback(new MqttCallback() {
//            @Override
//            public void connectionLost(Throwable throwable) {
//
//            }
//
//            @Override
//            public void messageArrived(String topic, MqttMessage message) {
//                processMessage(new String(message.getPayload()));
//            }
//
//            @Override
//            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
//
//            }
//            // 其他回调方法保持空实现
//        });
//    }
//
//    private void processMessage(String payload) {
//        JSONObject json = new JSONObject(payload);
//        JSONObject data = json.getJSONObject("data");
//        JSONObject values = data.getJSONObject("value");
//
//        HealthData healthData = new HealthData();
//        healthData.setSpo2(values.getInt("spo2"));
//        healthData.setBmp(values.getInt("bmp"));
//        healthData.setPi(values.getInt("pi"));
//        healthData.setDeviceCode(json.getString("deviceCode"));
//        healthData.setMac(data.getString("mac"));
//        healthData.setRecordTime(convertTimestamp(json.getLong("time")));
//        healthData.setUser_id(1);
//
//        dataService.addToBuffer(healthData);
//    }
//
//    private LocalDateTime convertTimestamp(long timestamp) {
//        return Instant.ofEpochSecond(timestamp)
//                .atZone(ZoneId.systemDefault())
//                .toLocalDateTime();
//    }
//
//    // WebSocket其他回调方法
//    @OnOpen
//    public void onOpen(Session session) {
//        System.out.println("WebSocket连接建立: " + session.getId());
//    }
//
//    @OnMessage
//    public void onMessage(String message, Session session) {
//        System.out.println("收到客户端消息: " + message);
//    }
//
//    @OnClose
//    public void onClose(Session session) {
//        System.out.println("WebSocket连接关闭: " + session.getId());
//     }
//}
package com.nurturing.ws;

import com.nurturing.Service.DataService;
//import com.nurturing.Service.impI.HealthDataService;
import com.nurturing.Service.impI.HealthDataService;
import com.nurturing.entity.HealthData;
import jakarta.annotation.PostConstruct;
import jakarta.websocket.OnClose;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import lombok.RequiredArgsConstructor;
import org.eclipse.paho.client.mqttv3.*;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@ServerEndpoint("/chat")
@Component
@RequiredArgsConstructor
public class ChatEndpoint {
    private static MqttClient mqttClient;
    private final HealthDataService dataService;

    @PostConstruct
    public void init() throws MqttException {
        mqttClient = new MqttClient("tcp://localhost:1888", "server-client");
        mqttClient.connect();
        mqttClient.subscribe("t/pub", 1);
        mqttClient.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage message) {
                processMessage(new String(message.getPayload()));
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
            // 其他回调方法保持空实现
        });
    }

    private void processMessage(String payload) {
        JSONObject json = new JSONObject(payload);
        JSONObject data = json.getJSONObject("data");
        JSONObject values = data.getJSONObject("value");

        HealthData healthData = new HealthData();
        healthData.setSpo2(values.getInt("spo2"));
        healthData.setBmp(values.getInt("bmp"));
        healthData.setPi(values.getInt("pi"));
        healthData.setDeviceCode(json.getString("deviceCode"));
        healthData.setMac(data.getString("mac"));
        healthData.setRecordTime(convertTimestamp(json.getLong("time")));

        dataService.addToBuffer(healthData);
    }

    private LocalDateTime convertTimestamp(long timestamp) {
        return Instant.ofEpochSecond(timestamp)
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    // WebSocket其他回调方法
    @OnOpen
    public void onOpen(Session session) {
        System.out.println("WebSocket连接建立: " + session.getId());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("收到客户端消息: " + message);
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("WebSocket连接关闭: " + session.getId());
    }
}
///**
// * @author : zq
// * @create 2024/8/21
// */
//@ServerEndpoint(value = "/chat")
//@Component
//public class ChatEndpoint {
//    /**
//     * 建立websocket连接后，被调用
//     *
//     */
//    private static MqttClient mqttClient;
//    private JSONObject datajson = new JSONObject();
////    private Map<String,String> map = new HashMap<>();
//    static {
//        try {
////            192.168.0.122
//            mqttClient = new MqttClient("tcp://localhost:1888", "javaweb000001");
//            mqttClient.connect();
//            String topic = "t/pub";
//            try {
//                mqttClient.subscribe(topic,1);
//                mqttClient.setCallback(new MqttCallback() {
//                    @Override
//                    public void connectionLost(Throwable throwable) {
//
//                    }
//
//                    @Override
//                    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
//                        String payload = new String(mqttMessage.getPayload());
//                        System.out.println(payload);
////                    session.getBasicRemote().sendText(payload);
//
//                    }
//
//                    @Override
//                    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
//
//                    }
//                });
//            } catch (MqttException e) {
//                throw new RuntimeException(e);
//            }
//        } catch (MqttException e) {
//            e.printStackTrace();
//        }
//    }
////    @OnOpen
////    public void onOpen(Session session) {
////        System.out.println("连接");
////        String topic = "t/pub";
////        try {
////            mqttClient.subscribe(topic,1);
////            mqttClient.setCallback(new MqttCallback() {
////                @Override
////                public void connectionLost(Throwable throwable) {
////
////                }
////
////                @Override
////                public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
////                    String payload = new String(mqttMessage.getPayload());
////                    System.out.println(payload);
//////                    session.getBasicRemote().sendText(payload);
////                    JSONObject json = new JSONObject(payload);
////                    System.out.println(json);
////                    JSONObject jsondata = json.getJSONObject("data");
////                    JSONObject value = jsondata.getJSONObject("value");
////                    int spo2 = value.getInt("spo2");
////                    datajson.put("spo2",spo2);
//////                    map.put("spo2", String.valueOf(spo2));
////                    long time = json.getLong("time");
////                    time*=1000;
////                    Date date = new Date(time);
////                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//////                    dateFormat.setTimeZone(TimeZone.getTimeZone("GM+8"));
////                    // 格式化日期
////                    String formattedDate = dateFormat.format(date);
////                    datajson.put("time",formattedDate);
////                    // 输出转换后的日期
////                    System.out.println("转换后的日期和时间: " + formattedDate);
////
////                    session.getBasicRemote().sendText(String.valueOf(datajson));
//////                    session.getBasicRemote().sendText(map.get("time"));
////                }
////
////                @Override
////                public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
////
////                }
////            });
////        } catch (MqttException e) {
////            throw new RuntimeException(e);
////        }
////    }
////
////
////
////
////    /**
////     * 浏览器发送消息到服务端，该方法被调用
////     * @param message
////     */
////    @OnMessage
////    public void onMessage(Session session ,String message) {
////
////    }
////
////    /**
////     * 断开 websocket 连接时被调用
////     *
////     */
////    @OnClose
////    public void onClose() {
////        try {
////            mqttClient.disconnect();
////        } catch (MqttException e) {
////            e.printStackTrace();
////        }
////
////    }
//}
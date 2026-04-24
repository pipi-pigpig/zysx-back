// package com.nurturing.mqtt;

// import com.fasterxml.jackson.core.JsonProcessingException;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.nurturing.Service.*;
// import com.nurturing.entity.BloodOxygen;
// import com.nurturing.entity.HeartRate;
// import com.nurturing.entity.PerfusionIndex;
// import com.nurturing.entity.SleepData;
// import org.eclipse.paho.client.mqttv3.*;
// import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Service;
// import jakarta.annotation.PostConstruct;
// import jakarta.annotation.PreDestroy;

// import java.math.BigDecimal;
// import java.util.Map;
// import java.util.concurrent.atomic.AtomicInteger;

// @Service
// public class MqttService {

//     private static final Logger log = LoggerFactory.getLogger(MqttService.class);

//     @Value("${mqtt.broker}")
//     private String brokerUrl;

//     @Value("${mqtt.client.id}")
//     private String clientId;

//     @Value("${mqtt.username:}")
//     private String username;

//     @Value("${mqtt.password:}")
//     private String password;

//     @Value("${mqtt.connection.timeout}")
//     private int connectionTimeout;

//     @Value("${mqtt.keep.alive.interval}")
//     private int keepAliveInterval;

//     @Value("${mqtt.max.reconnect.attempts}")
//     private int maxReconnectAttempts;

//     @Value("${mqtt.reconnect.delay.seconds}")
//     private int reconnectDelaySeconds;

//     @Value("#{'${mqtt.topic.device.data}'.split(',')}") // 如果有多个主题，按逗号分割
//     private String[] topicsToSubscribe;

//     @Autowired
//     private BloodOxygenService bloodOxygenService;

//     @Autowired
//     private BloodPressureService bloodPressureService;

//     @Autowired
//     private BloodSugarService bloodSugarService;

//     @Autowired
//     private HeartRateService heartRateService;

//     @Autowired
//     private PerfusionIndexService perfusionIndexService;

//     @Autowired
//     private SleepDataService sleepDataService;

//     @Autowired
//     private FieldMappingService fieldMappingService;

//     private MqttClient mqttClient;
//     private final AtomicInteger reconnectAttemptCount = new AtomicInteger(0);
//     private volatile boolean shouldReconnect = true;
//     private final ObjectMapper objectMapper = new ObjectMapper();

//     @PostConstruct
//     public void init() {
//         log.info("开始初始化MQTT服务...");
//         connectAndSubscribe();
//     }

//     private void connectAndSubscribe() {
//         try {
//             log.info("正在尝试连接到MQTT服务器: {}", brokerUrl);
//             mqttClient = new MqttClient(brokerUrl, clientId, new MemoryPersistence());

//             MqttConnectOptions options = new MqttConnectOptions();
//             options.setCleanSession(true);
//             options.setConnectionTimeout(connectionTimeout);
//             options.setKeepAliveInterval(keepAliveInterval);
//             if (!username.isEmpty() && !password.isEmpty()) {
//                 options.setUserName(username);
//                 options.setPassword(password.toCharArray());
//             }

//             mqttClient.setCallback(new MqttCallback() {
//                 @Override
//                 public void connectionLost(Throwable cause) {
//                     log.warn("MQTT连接丢失: {}", cause.getMessage());
//                     if (shouldReconnect) {
//                         reconnect();
//                     } else {
//                         log.error("已达到最大重连尝试次数。MQTT服务已停止。");
//                     }
//                 }

//                 @Override
//                 public void messageArrived(String topic, MqttMessage message) throws Exception {
//                     log.debug("收到MQTT消息，主题: {}", topic);
//                     handleMessage(topic, message.getPayload());
//                 }

//                 @Override
//                 public void deliveryComplete(IMqttDeliveryToken token) {
//                     // 消息发送完成的回调，此处可留空或添加逻辑
//                 }
//             });

//             mqttClient.connect(options);

//             for (String topic : topicsToSubscribe) {
//                 log.info("正在订阅主题: {}", topic);
//                 mqttClient.subscribe(topic, 1); // QoS 1
//             }
//             log.info("MQTT服务连接并订阅成功。");
//             reconnectAttemptCount.set(0); // 连接成功后，重置重连计数器

//         } catch (MqttException e) {
//             log.error("连接到MQTT服务器失败: {}", e.getMessage(), e);
//             handleConnectionFailure();
//         }
//     }

//     private void handleMessage(String topic, byte[] payload) {
//         String payloadStr = new String(payload);
//         // 1. 在解析前记录原始消息（如果需要，可以先记录，但为了调试，可以放在解析后）
//         log.debug("处理原始消息: {}", payloadStr);

//         try {
//             // 2. 解析整个消息体为Map
//             @SuppressWarnings("unchecked")
//             Map<String, Object> messageMap = objectMapper.readValue(payloadStr, Map.class);

//             // 3. 检查消息类型是否为 "reportAttribute"
//             String type = (String) messageMap.get("type");
//             if (!"reportAttribute".equals(type)) {
//                 log.debug("消息类型 [{}] 不是 'reportAttribute'，忽略此消息。", type);
//                 return; // 直接返回，不进行后续处理
//             }

//             log.debug("消息类型为 'reportAttribute'，继续处理。");

//             // 4. 获取数据部分
//             @SuppressWarnings("unchecked")
//             Map<String, Object> dataMap = (Map<String, Object>) messageMap.get("data");
//             if (dataMap == null) {
//                 log.warn("消息体中 'data' 部分为空，忽略此消息。主题: {}", topic);
//                 return; // 直接返回
//             }

//             // 5. 提取设备类型 (attribute) 和MAC地址 (来自 data.mac)
//             String deviceName = (String) dataMap.get("attribute");
//             String deviceMac = (String) dataMap.get("mac"); // 从 data 对象内部获取 MAC

//             if (deviceName == null || deviceMac == null) {
//                 log.warn("消息体中 'data.attribute' 或 'data.mac' 为空，无法确定设备类型或MAC，忽略此消息。主题: {}, data: {}", topic, dataMap);
//                 return; // 直接返回
//             }

//             log.debug("提取到设备类型 [{}] 和 MAC [{}]，继续处理。", deviceName, deviceMac);

//             // 6. 检查设备类型 (attribute) 是否在 field_mappings.json 中注册 (第一级键)
//             //    FieldMappingService 的 isMacAllowed 方法内部会先检查 deviceName 是否存在
//             //    如果不存在，isMacAllowed 会返回 false
//             if (!fieldMappingService.isMacAllowed(deviceName, deviceMac)) {
//                 // isMacAllowed 内部已经检查了 deviceName 是否存在，以及 mac 是否在列表中
//                 // 如果 deviceName 不存在，它会返回 false
//                 if (!fieldMappingService.getAllMappings().containsKey(deviceName)) {
//                     log.info("设备类型 [{}] 未在 field_mappings.json 中注册，忽略此消息。MAC: [{}]", deviceName, deviceMac);
//                 } else {
//                     // 如果 deviceName 存在，但 MAC 不在 allowedMacs 中
//                     log.info("设备 [{}] (MAC: {}) 的MAC地址未在允许列表中，忽略此消息。", deviceName, deviceMac);
//                 }
//                 return; // 直接返回
//             }

//             log.debug("设备 [{}] (MAC: {}) 通过了类型和MAC白名单检查。", deviceName, deviceMac);

//             // 7. 获取原始数据值 (value部分)
//             @SuppressWarnings("unchecked")
//             Map<String, Object> valueMap = (Map<String, Object>) dataMap.get("value");
//             if (valueMap == null || valueMap.isEmpty()) {
//                 log.warn("设备 [{}] (MAC: {}) 的 'data.value' 部分为空，忽略此消息。", deviceName, deviceMac);
//                 return; // 直接返回
//             }

//             // 8. 获取该设备类型的字段到健康数据类型的映射
//             Map<String, HealthDataType> fieldToTypeMapping = fieldMappingService.getFieldToTypeMappingsForDevice(deviceName);

//             if (fieldToTypeMapping.isEmpty()) {
//                 log.warn("设备 [{}] (MAC: {}) 在 field_mappings.json 中没有配置 fieldMappings，无法处理数据。", deviceName, deviceMac);
//                 return; // 直接返回
//             }

//             log.debug("设备 [{}] (MAC: {}) 使用字段映射: {}", deviceName, deviceMac, fieldToTypeMapping);

//             // 9. 遍历原始数据值，根据映射关系分发到不同的服务方法
//             for (Map.Entry<String, Object> valueEntry : valueMap.entrySet()) {
//                 String rawFieldName = valueEntry.getKey();
//                 Object rawFieldValue = valueEntry.getValue();

//                 HealthDataType dataType = fieldToTypeMapping.get(rawFieldName);
//                 if (dataType != null) {
//                     // 创建一个只包含当前处理字段的Map
//                     Map<String, Object> singleFieldMap = Map.of(rawFieldName, rawFieldValue);
//                     String singleFieldJsonStr = objectMapper.writeValueAsString(singleFieldMap);

//                     log.debug("设备 [{}] (MAC: {}) 的字段 [{}] 映射到健康数据类型 [{}]，准备存储: {}", deviceName, deviceMac, rawFieldName, dataType, singleFieldJsonStr);

//                     // 根据健康数据类型调用相应的服务方法
//                     switch (dataType) {
//                         case BLOOD:
// //                            log.info("BLOOD");
//                             sleepDataService.saveFromMQTT(new SleepData(new BigDecimal(rawFieldValue.toString())),deviceMac);
//                             log.debug("设备 [{}] (MAC: {}) 的血圧数据已通过服务层方法存储。", deviceName, deviceMac);
//                             break;
//                         case HEART:
// //                            log.info("HEART");
//                             heartRateService.saveFromMQTT(new HeartRate(new BigDecimal(rawFieldValue.toString())),deviceMac);
//                             log.debug("设备 [{}] (MAC: {}) 的心率数据已通过服务层方法存储。", deviceName, deviceMac);
//                             break;
//                         case OXYGEN:
// //                            log.info("OXYGEN");
//                             bloodOxygenService.saveFromMQTT(new BloodOxygen(new BigDecimal(rawFieldValue.toString())),deviceMac);
//                             log.debug("设备 [{}] (MAC: {}) 的血氧数据已通过服务层方法存储。", deviceName, deviceMac);
//                             break;
//                         case PI:
// //                            log.info("PI");
//                             perfusionIndexService.saveFromMQTT(new PerfusionIndex(new BigDecimal(rawFieldValue.toString())),deviceMac);
//                             log.debug("设备 [{}] (MAC: {}) 的灌注指数数据已通过服务层方法存储。", deviceName, deviceMac);
//                             break;
//                         case PRESSURE:
//                             log.info("PRESSURE---未处理");
// //                            bloodPressureService.saveFromMQTT();
//                             log.debug("设备 [{}] (MAC: {}) 的压力数据已通过服务层方法存储。", deviceName, deviceMac);
//                             break;
//                         case SLEEP:
// //                            log.info("SLEEP");
//                             sleepDataService.saveFromMQTT(new SleepData(new BigDecimal(rawFieldValue.toString())),deviceMac);
//                             log.debug("设备 [{}] (MAC: {}) 的睡眠数据已通过服务层方法存储。", deviceName, deviceMac);
//                             break;
//                         default:
//                             log.warn("未处理的健康数据类型 [{}]，设备 [{}] (MAC: {})，字段 [{}]", dataType, deviceName, deviceMac, rawFieldName);
//                             break;
//                     }
//                 } else {
//                     // 如果原始字段名不在映射中，可以选择忽略或记录
//                     log.debug("设备 [{}] (MAC: {}) 的字段 [{}] 在 field_mappings.json 中未找到映射，忽略此字段。", deviceName, deviceMac, rawFieldName);
//                 }
//             }

//             log.debug("设备 [{}] (MAC: {}) 的消息处理完成。", deviceName, deviceMac);

//         } catch (JsonProcessingException e) {
//             log.error("解析MQTT消息体失败 (可能不是有效的JSON): {}", e.getMessage(), e);
//             log.debug("出错的消息内容: {}", payloadStr); // 记录出错的消息内容，方便调试
//         } catch (Exception e) {
//             log.error("处理MQTT消息时发生未知错误: {}", e.getMessage(), e);
//             log.debug("出错的消息内容: {}", payloadStr); // 记录出错的消息内容，方便调试
//         }
//     }


//     private void reconnect() {
//         if (reconnectAttemptCount.incrementAndGet() <= maxReconnectAttempts && shouldReconnect) {
//             log.info("正在尝试重连 (尝试次数 {}/{}). 等待 {} 秒...", reconnectAttemptCount.get(), maxReconnectAttempts, reconnectDelaySeconds);
//             try {
//                 Thread.sleep(reconnectDelaySeconds * 1000L);
//             } catch (InterruptedException e) {
//                 Thread.currentThread().interrupt();
//                 log.warn("重连尝试被中断。");
//                 return;
//             }
//             connectAndSubscribe(); // 递归调用连接方法
//         } else {
//             log.error("已达到最大重连尝试次数 ({})。放弃MQTT服务。", maxReconnectAttempts);
//             shouldReconnect = false; // 停止进一步尝试
//         }
//     }

//     private void handleConnectionFailure() {
//         if (reconnectAttemptCount.incrementAndGet() <= maxReconnectAttempts) {
//             log.warn("初始连接失败 (尝试次数 {}/{}). {} 秒后安排重试...", reconnectAttemptCount.get(), maxReconnectAttempts, reconnectDelaySeconds);
//             new Thread(() -> {
//                 try {
//                     Thread.sleep(reconnectDelaySeconds * 1000L);
//                     connectAndSubscribe();
//                 } catch (InterruptedException e) {
//                     Thread.currentThread().interrupt();
//                     log.warn("初始连接重试被中断。");
//                 }
//             }).start();
//         } else {
//             log.error("已达到最大初始连接尝试次数 ({})。MQTT服务将不会启动。", maxReconnectAttempts);
//             shouldReconnect = false; // 防止后续重连尝试
//         }
//     }

//     @PreDestroy
//     public void cleanup() {
//         log.info("正在清理MQTT服务资源...");
//         shouldReconnect = false; // 信号：停止重连尝试
//         if (mqttClient != null && mqttClient.isConnected()) {
//             try {
//                 mqttClient.disconnect(); // 优雅断开连接
//                 log.info("已从MQTT服务器优雅断开连接。");
//             } catch (MqttException e) {
//                 log.error("断开MQTT连接时出错: {}", e.getMessage(), e);
//             }
//         }
//         try {
//             if (mqttClient != null) {
//                 mqttClient.close();
//             }
//         } catch (MqttException e) {
//             log.error("关闭MQTT客户端时出错: {}", e.getMessage(), e);
//         }
//     }
// }

// package com.nurturing.mqtt;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.core.io.Resource;
// import org.springframework.stereotype.Service;
// import jakarta.annotation.PostConstruct;
// import java.io.IOException;
// import java.io.InputStream;
// import java.util.List;
// import java.util.Map;
// import java.util.concurrent.ConcurrentHashMap;

// @Service
// public class FieldMappingService {

//     private static final Logger log = LoggerFactory.getLogger(FieldMappingService.class);

//     @Value("classpath:field_mappings.json")
//     private Resource mappingFileResource;

//     private final ObjectMapper objectMapper = new ObjectMapper();
//     // 结构: deviceName -> { "allowedMacs": [...], "fieldMappings": { rawFieldName -> HealthDataType } }
//     private Map<String, Map<String, Object>> deviceMappings = new ConcurrentHashMap<>();

//     @PostConstruct
//     public void loadMappings() {
//         try (InputStream inputStream = mappingFileResource.getInputStream()) {
//             @SuppressWarnings("unchecked")
//             Map<String, Map<String, Object>> loadedMappings = objectMapper.readValue(inputStream, Map.class);
//             this.deviceMappings.putAll(loadedMappings);
//             log.info("成功从文件 {} 加载设备映射配置: {}", mappingFileResource.getFilename(), deviceMappings.keySet());
//         } catch (IOException e) {
//             log.error("从文件 {} 加载设备映射配置失败: {}", mappingFileResource.getFilename(), e.getMessage(), e);
//             this.deviceMappings = new ConcurrentHashMap<>();
//         }
//     }

//     /**
//      * 检查指定设备类型的MAC地址是否被允许
//      * @param deviceName 设备类型名称 (对应 data.attribute)
//      * @param mac 设备MAC地址
//      * @return 如果允许，返回true；否则返回false
//      */
//     public boolean isMacAllowed(String deviceName, String mac) {
//         Map<String, Object> mappingConfig = deviceMappings.get(deviceName);
//         if (mappingConfig != null) {
//             @SuppressWarnings("unchecked")
//             List<String> allowedMacs = (List<String>) mappingConfig.get("allowedMacs");
//             if (allowedMacs != null) {
//                 return allowedMacs.contains(mac);
//             }
//         }
//         return false;
//     }

//     /**
//      * 获取指定设备类型的字段到健康数据类型的映射
//      * @param deviceName 设备类型名称 (对应 data.attribute)
//      * @return 字段到HealthDataType的映射Map，如果未找到则返回空Map
//      */
//     public Map<String, HealthDataType> getFieldToTypeMappingsForDevice(String deviceName) {
//         Map<String, Object> mappingConfig = deviceMappings.get(deviceName);
//         if (mappingConfig != null) {
//             @SuppressWarnings("unchecked")
//             Map<String, String> rawFieldMappings = (Map<String, String>) mappingConfig.get("fieldMappings");
//             if (rawFieldMappings != null) {
//                 Map<String, HealthDataType> typedFieldMappings = new java.util.HashMap<>();
//                 for (Map.Entry<String, String> entry : rawFieldMappings.entrySet()) {
//                     String rawFieldName = entry.getKey();
//                     String typeName = entry.getValue();
//                     try {
//                         HealthDataType type = HealthDataType.fromValue(typeName);
//                         typedFieldMappings.put(rawFieldName, type);
//                     } catch (IllegalArgumentException e) {
//                         log.warn("在设备 [{}] 的映射中发现未知的健康数据类型 [{}]，忽略该字段 [{}]", deviceName, typeName, rawFieldName);
//                     }
//                 }
//                 return typedFieldMappings;
//             }
//         }
//         return Map.of();
//     }

//     /**
//      * 获取所有设备类型的映射
//      * @return 所有设备类型的映射Map
//      */
//     public Map<String, Map<String, Object>> getAllMappings() {
//         return deviceMappings;
//     }
// }
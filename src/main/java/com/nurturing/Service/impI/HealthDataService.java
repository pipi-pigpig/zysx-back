//package com.nurturing.Service.impI;
//
//import com.nurturing.Mapper.HealthDataMapper;
//import com.nurturing.entity.HealthData;
//import com.nurturing.entity.HeartData;
//import com.nurturing.entity.OxygenData;
//import com.nurturing.entity.PiData;
//import com.nurturing.event.HealthDataUpdatedEvent;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.ApplicationEventPublisher;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.concurrent.BlockingQueue;
//import java.util.concurrent.LinkedBlockingQueue;
//import java.util.concurrent.atomic.AtomicInteger;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class HealthDataService {
//    private final HealthDataMapper mapper;
//    private final ApplicationEventPublisher eventPublisher;
//    private final BlockingQueue<HealthData> buffer = new LinkedBlockingQueue<>();
//    private final AtomicInteger currentId = new AtomicInteger(1);
//    private volatile HealthData latestData;
//
//    @Scheduled(fixedRate = 2000, initialDelay = 1000)
//    @Async("taskExecutor")
//    @Transactional(rollbackFor = Exception.class)
//    public void processBuffer() {
//        try {
//            // 处理所有可用的数据，而不是只处理一条
//            while (!buffer.isEmpty()) {
//                HealthData data = buffer.poll();
//                if (data != null) {
//                    log.debug("Processing health data: {}", data);
//
//                    int nextId = currentId.getAndUpdate(prev -> (prev % 10) + 1);
//                    data.setId(nextId);
//
//                    // 执行数据库操作
//                    mapper.replace(data);
//
//                    // 创建并保存 OxygenData
//                    OxygenData oxygenData = new OxygenData();
//                    oxygenData.setUser_id(data.getUser_id());
//                    oxygenData.setCreated_at(data.getRecordTime());
//                    oxygenData.setOxygenData(data.getSpo2());
//                    oxygenData.setBloodOxygenID(nextId);
//                    mapper.replaceOxygen(oxygenData);
//
//                    // 创建并保存 HeartData
//                    HeartData heartData = new HeartData();
//                    heartData.setUser_id(data.getUser_id());
//                    heartData.setCreated_at(data.getRecordTime());
//                    heartData.setHeartData(data.getBmp());
//                    heartData.setHeartRateID(nextId);
//                    mapper.replaceHeart(heartData);
//
//                    // 创建并保存 PiData
//                    PiData piData = new PiData();
//                    piData.setUser_id(data.getUser_id());
//                    piData.setCreated_at(data.getRecordTime());
//                    piData.setPiData(data.getPi());
//                    piData.setPerfusionIndexID(nextId);
//                    mapper.replacePi(piData);
//
//                    latestData = data;
//
//                    // 发布事件通知 WebSocket
//                    try {
//                        eventPublisher.publishEvent(new HealthDataUpdatedEvent(this, data));
//                    } catch (Exception e) {
//                        log.error("Error publishing event: {}", e.getMessage(), e);
//                    }
//
//                    log.info("Successfully processed health data for device: {}", data.getDeviceCode());
//                }
//            }
//        } catch (Exception e) {
//            log.error("Error in processBuffer: {}", e.getMessage(), e);
//        }
//    }
//
//    public void addToBuffer(HealthData data) {
//        try {
//            if (buffer.offer(data)) {
//                log.debug("Added to buffer: {}", data);
//            } else {
//                log.warn("Failed to add to buffer, queue might be full: {}", data);
//            }
//        } catch (Exception e) {
//            log.error("Error adding to buffer: {}", e.getMessage(), e);
//        }
//    }
//
//    public HealthData getLatestData() {
//        return latestData;
//    }
//
//    // 获取缓冲区大小（用于监控）
//    public int getBufferSize() {
//        return buffer.size();
//    }
//}
package com.nurturing.Event;

import com.nurturing.Handler.BloodOxygenWebSocketHandler;
import com.nurturing.Handler.HeartRateWebSocketHandler;
import com.nurturing.Handler.PerfusionIndexWebSocketHandler;
import com.nurturing.entity.BloodOxygen;
import com.nurturing.entity.HeartRate;
import com.nurturing.entity.PerfusionIndex;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * @author ZhangQinAn
 * @email 242646968@qq.com
 * @since 2025/10/22 上午11:03
 */
@Component
@Slf4j
public class HealthDataEventListener {

    @Autowired
    private BloodOxygenWebSocketHandler bloodOxygenWebSocketHandler;

    @Autowired
    private HeartRateWebSocketHandler heartRateWebSocketHandler;

    @Autowired
    private PerfusionIndexWebSocketHandler perfusionIndexWebSocketHandler;

    // 监听血氧数据事件
    @EventListener
    public void handleBloodOxygenDataEvent(BloodOxygenDataEvent event) {
        BloodOxygen bloodOxygen = event.getHealthData();
        bloodOxygenWebSocketHandler.pushDataToUser(
                String.valueOf(bloodOxygen.getUserId()),
                Collections.singletonList(bloodOxygen)
        );
        log.info("已推送血氧数据给用户: {}", bloodOxygen.getUserId());
    }


     @EventListener
     public void handleHeartRateDataEvent(HeartRateDataEvent event) {
         HeartRate heartRate = event.getHealthData();
         heartRateWebSocketHandler.pushDataToUser(
             String.valueOf(heartRate.getUserId()),
             Collections.singletonList(heartRate)
         );
     }

    @EventListener
    public void handlePerfusionIndexDataEvent(PerfusionIndexDataEvent event) {
        PerfusionIndex perfusionIndex = event.getHealthData();
        perfusionIndexWebSocketHandler.pushDataToUser(
                String.valueOf(perfusionIndex.getUserId()),
                Collections.singletonList(perfusionIndex)
        );
    }
}

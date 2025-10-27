package com.nurturing.Event;

import com.nurturing.Handler.*;
import com.nurturing.entity.*;
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

    @Autowired
    private BloodPressureWebSocketHandler bloodPressureWebSocketHandler;

    @Autowired
    private BloodSugarWebSocketHandler bloodSugarWebSocketHandler;

    @Autowired
    private SleepDataWebSocketHandler sleepDataWebSocketHandler;

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

    @EventListener
    public void handleBloodPressureDataEvent(BloodPressureDataEvent event) {
        BloodPressure bloodPressure = event.getHealthData();
        bloodPressureWebSocketHandler.pushDataToUser(
                String.valueOf(bloodPressure.getUserId()),
                Collections.singletonList(bloodPressure)
        );
    }

    @EventListener
    public void handleBloodSugarDataEvent(BloodSugarDataEvent event) {
        BloodSugar bloodSugar = event.getHealthData();
        bloodSugarWebSocketHandler.pushDataToUser(
                String.valueOf(bloodSugar.getUserId()),
                Collections.singletonList(bloodSugar)
        );
    }

    @EventListener
    public void handleSleepDataEvent(SleepDataEvent event) {
        SleepData sleepData = event.getHealthData();
        sleepDataWebSocketHandler.pushDataToUser(
                String.valueOf(sleepData.getUserId()),
                Collections.singletonList(sleepData)
        );
    }
}

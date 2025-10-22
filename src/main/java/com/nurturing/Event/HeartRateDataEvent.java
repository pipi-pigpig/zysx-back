package com.nurturing.Event;

import com.nurturing.entity.HeartRate;

/**
 * @author ZhangQinAn
 * @email 242646968@qq.com
 * @since 2025/10/22 上午10:58
 */

public class HeartRateDataEvent extends HealthDataEvent<HeartRate> {
    public HeartRateDataEvent(Object source, HeartRate heartRate) {
        super(source, heartRate, "heartRate");
    }
}

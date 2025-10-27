package com.nurturing.Event;

import com.nurturing.entity.BloodOxygen;
import com.nurturing.entity.SleepData;

/**
 * @author ZhangQinAn
 * @email 242646968@qq.com
 * @since 2025/10/27 上午10:47
 */

public class SleepDataEvent extends HealthDataEvent<SleepData> {

    public SleepDataEvent(Object source, SleepData sleepData) {
        super(source, sleepData, "sleepData");
    }
}

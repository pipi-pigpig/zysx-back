package com.nurturing.Event;

import com.nurturing.entity.BloodOxygen;
import com.nurturing.entity.BloodPressure;

/**
 * @author ZhangQinAn
 * @email 242646968@qq.com
 * @since 2025/10/27 上午10:46
 */
public class BloodPressureDataEvent extends HealthDataEvent<BloodPressure> {

    public BloodPressureDataEvent(Object source, BloodPressure bloodPressure) {
        super(source, bloodPressure, "bloodPressure");
    }
}

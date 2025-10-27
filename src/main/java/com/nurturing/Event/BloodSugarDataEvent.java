package com.nurturing.Event;

import com.nurturing.entity.BloodOxygen;
import com.nurturing.entity.BloodSugar;

/**
 * @author ZhangQinAn
 * @email 242646968@qq.com
 * @since 2025/10/27 上午10:46
 */
public class BloodSugarDataEvent extends HealthDataEvent<BloodSugar>{

    public BloodSugarDataEvent(Object source,BloodSugar bloodSugar) {
        super(source, bloodSugar, "bloodSugar");
    }
}

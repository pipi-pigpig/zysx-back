package com.nurturing.Event;

/**
 * @author ZhangQinAn
 * @email 242646968@qq.com
 * @since 2025/10/22 上午8:57
 */
import com.nurturing.entity.BloodOxygen;
import org.springframework.context.ApplicationEvent;

// 保留原有的血氧数据事件，用于兼容性
public class BloodOxygenDataEvent extends HealthDataEvent<BloodOxygen> {
    public BloodOxygenDataEvent(Object source, BloodOxygen bloodOxygen) {
        super(source, bloodOxygen, "bloodOxygen");
    }
}



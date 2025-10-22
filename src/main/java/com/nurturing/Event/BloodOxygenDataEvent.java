package com.nurturing.Event;

/**
 * @author ZhangQinAn
 * @email 242646968@qq.com
 * @since 2025/10/22 上午8:57
 */
import com.nurturing.entity.BloodOxygen;
import org.springframework.context.ApplicationEvent;

public class BloodOxygenDataEvent extends ApplicationEvent {
    private final BloodOxygen bloodOxygen;

    public BloodOxygenDataEvent(Object source, BloodOxygen bloodOxygen) {
        super(source);
        this.bloodOxygen = bloodOxygen;
    }

    public BloodOxygen getBloodOxygen() {
        return bloodOxygen;
    }
}

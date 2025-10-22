package com.nurturing.Event;

import com.nurturing.Service.HealthData;
import org.springframework.context.ApplicationEvent;

/**
 * @author ZhangQinAn
 * @email 242646968@qq.com
 * @since 2025/10/22 上午10:55
 */
// 通用健康数据事件
public class HealthDataEvent<T extends HealthData> extends ApplicationEvent {
    private final T healthData;
    private final String dataType;

    public HealthDataEvent(Object source, T healthData, String dataType) {
        super(source);
        this.healthData = healthData;
        this.dataType = dataType;
    }

    public T getHealthData() {
        return healthData;
    }

    public String getDataType() {
        return dataType;
    }
}

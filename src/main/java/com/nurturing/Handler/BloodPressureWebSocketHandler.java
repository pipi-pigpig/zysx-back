package com.nurturing.Handler;

import com.nurturing.Service.BloodPressureService;
import com.nurturing.entity.BloodOxygen;
import com.nurturing.entity.BloodPressure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author ZhangQinAn
 * @email 242646968@qq.com
 * @since 2025/10/27 上午10:35
 */
@Component
public class BloodPressureWebSocketHandler extends BaseHealthDataWebSocketHandler<BloodPressure>{

    @Autowired
    private BloodPressureService bloodPressureService;

    @Override
    protected List<BloodPressure> getRecentData(Long userId) {
        return bloodPressureService.getRecentData(userId);
    }
    @Override
    protected String getDataType() {
        return "bloodPressure";
    }
}

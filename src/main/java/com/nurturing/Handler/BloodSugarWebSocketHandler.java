package com.nurturing.Handler;

import com.nurturing.Service.BloodSugarService;
import com.nurturing.entity.BloodOxygen;
import com.nurturing.entity.BloodSugar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author ZhangQinAn
 * @email 242646968@qq.com
 * @since 2025/10/27 上午10:36
 */
@Component
public class BloodSugarWebSocketHandler extends BaseHealthDataWebSocketHandler<BloodSugar>{

    @Autowired
    private BloodSugarService bloodSugarService;

    @Override
    protected List<BloodSugar> getRecentData(Long userId) {
        return bloodSugarService.getRecentData(userId);
    }

    @Override
    protected String getDataType() {
        return "bloodSugar";
    }
}

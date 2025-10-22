package com.nurturing.Handler;

import com.nurturing.Service.HeartRateService;
import com.nurturing.entity.HeartRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author ZhangQinAn
 * @email 242646968@qq.com
 * @since 2025/10/22 上午10:35
 */
@Component
public class HeartRateWebSocketHandler extends BaseHealthDataWebSocketHandler<HeartRate> {

    @Autowired
    private HeartRateService heartRateService;

    @Override
    protected List<HeartRate> getRecentData(Long userId) {
        return heartRateService.getRecentData(userId);
    }

    @Override
    protected String getDataType() {
        return "heartRate";
    }
}

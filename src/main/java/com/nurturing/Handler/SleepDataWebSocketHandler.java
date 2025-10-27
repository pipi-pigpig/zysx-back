package com.nurturing.Handler;

import com.nurturing.Service.SleepDataService;
import com.nurturing.entity.BloodOxygen;
import com.nurturing.entity.SleepData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author ZhangQinAn
 * @email 242646968@qq.com
 * @since 2025/10/27 上午10:36
 */
@Component
public class SleepDataWebSocketHandler extends BaseHealthDataWebSocketHandler<SleepData> {

    @Autowired
    private SleepDataService sleepDataService;

    @Override
    protected List<SleepData> getRecentData(Long userId) {
        return sleepDataService.getRecentData(userId);
    }

    @Override
    protected String getDataType() {
        return "sleepData";
    }
}

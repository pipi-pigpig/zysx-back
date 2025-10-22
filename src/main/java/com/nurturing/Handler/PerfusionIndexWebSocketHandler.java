package com.nurturing.Handler;

import com.nurturing.Service.PerfusionIndexService;
import com.nurturing.entity.PerfusionIndex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author ZhangQinAn
 * @email 242646968@qq.com
 * @since 2025/10/22 上午10:35
 */
@Component
public class PerfusionIndexWebSocketHandler extends BaseHealthDataWebSocketHandler<PerfusionIndex> {

    @Autowired
    private PerfusionIndexService perfusionIndexService;

    @Override
    protected List<PerfusionIndex> getRecentData(Long userId) {
        return perfusionIndexService.getRecentData(userId);
    }

    @Override
    protected String getDataType() {
        return "perfusionIndex";
    }
}

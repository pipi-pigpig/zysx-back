package com.nurturing.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nurturing.entity.PerfusionIndex;

import java.util.List;

public interface PerfusionIndexService extends HealthDataService<PerfusionIndex> {
    List<PerfusionIndex> getById(Long userId);

    void saveFromMQTT(PerfusionIndex perfusionIndex,String mac);
}

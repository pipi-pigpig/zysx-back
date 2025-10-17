package com.nurturing.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nurturing.entity.SleepData;

import java.util.List;

public interface SleepDataService extends IService<SleepData> {
    List<SleepData> getById(Long userId);

    void saveFromMQTT(SleepData sleepData,String mac);
}

package com.nurturing.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nurturing.entity.HeartRate;

import java.util.List;

public interface HeartRateService extends IService<HeartRate> {

    List<HeartRate> getById(Long userId);

    void saveFromMQTT(HeartRate heartRate,String mac);
}

package com.nurturing.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nurturing.entity.BloodPressure;

import java.util.List;

public interface BloodPressureService extends IService<BloodPressure> {
    List<BloodPressure> getById(Long userId);

    void saveFromMQTT(BloodPressure bloodPressure,String mac);
}

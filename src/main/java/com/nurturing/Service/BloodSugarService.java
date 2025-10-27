package com.nurturing.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nurturing.entity.BloodSugar;

import java.util.List;

public interface BloodSugarService extends HealthDataService<BloodSugar> {
    List<BloodSugar> getById(Long userId);

    void saveFromMQTT(BloodSugar bloodData,String mac);
}

package com.nurturing.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nurturing.entity.BloodOxygen;

import java.util.List;

public interface BloodOxygenService extends IService<BloodOxygen> {
    List<BloodOxygen> getById(Long userId);

    void saveFromMQTT(BloodOxygen bloodOxygen, String mac);
}

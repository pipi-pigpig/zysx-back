package com.nurturing.Service.impI;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nurturing.Event.BloodOxygenDataEvent;
import com.nurturing.Event.BloodPressureDataEvent;
import com.nurturing.Mapper.BloodPressureMapper;
import com.nurturing.Service.BloodPressureService;
import com.nurturing.entity.BloodOxygen;
import com.nurturing.entity.BloodPressure;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class BloodPressureServiceImpl extends ServiceImpl<BloodPressureMapper,BloodPressure> implements BloodPressureService {


    @Autowired
    private BloodPressureMapper bloodPressureMapper;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Override
    public List<BloodPressure> getById(Long userId) {
        return bloodPressureMapper.getById(userId);
    }

    @Override
    public List<BloodPressure> getRecentData(Long userId) {
        return bloodPressureMapper.getRecentData(userId, 10);
    }

    @Override
    @Transactional
    public boolean save(BloodPressure entity) {
        boolean result = super.save(entity);
        if (result) {
            // 发布血压数据事件，触发WebSocket推送
            eventPublisher.publishEvent(new BloodPressureDataEvent(this, entity));
        }
        return result;
    }
    @Override
    @Transactional
    public void saveHealthData(BloodPressure entity) {
        save(entity);
    }


    @Override
    @Transactional
    public void saveFromMQTT(BloodPressure bloodPressure, String mac) {
        bloodPressureMapper.insertByMac(bloodPressure.getSystolicBp(),bloodPressure.getDiastolicBp(),mac, LocalDateTime.now());
    }
}

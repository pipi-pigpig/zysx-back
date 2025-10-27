package com.nurturing.Service.impI;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nurturing.Event.BloodOxygenDataEvent;
import com.nurturing.Event.BloodSugarDataEvent;
import com.nurturing.Mapper.BloodSugarMapper;
import com.nurturing.Service.BloodSugarService;
import com.nurturing.entity.BloodOxygen;
import com.nurturing.entity.BloodSugar;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class BloodSugarServiceImpl extends ServiceImpl<BloodSugarMapper,BloodSugar> implements BloodSugarService {

    @Autowired
    private BloodSugarMapper bloodSugarMapper;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Override
    public List<BloodSugar> getById(Long userId) {

        return bloodSugarMapper.getById(userId);
    }

    @Override
    public List<BloodSugar> getRecentData(Long userId) {
        return bloodSugarMapper.getRecentData(userId, 10);
    }

    @Override
    @Transactional
    public boolean save(BloodSugar entity) {
        boolean result = super.save(entity);
        if (result) {
            // 发布血糖数据事件，触发WebSocket推送
            eventPublisher.publishEvent(new BloodSugarDataEvent(this, entity));
        }
        return result;
    }
    @Override
    @Transactional
    public void saveHealthData(BloodSugar entity) {
        save(entity);
    }

    @Override
    @Transactional
    public void saveFromMQTT(BloodSugar bloodData, String mac) {
        bloodSugarMapper.insertByMac(bloodData.getBloodData(),mac, LocalDateTime.now());
    }
}

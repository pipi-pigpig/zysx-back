package com.nurturing.Service.impI;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nurturing.Event.BloodOxygenDataEvent;
import com.nurturing.Event.SleepDataEvent;
import com.nurturing.Mapper.SleepDataMapper;
import com.nurturing.Service.SleepDataService;
import com.nurturing.entity.BloodOxygen;
import com.nurturing.entity.SleepData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class SleepDataServiceImpl extends ServiceImpl<SleepDataMapper,SleepData> implements SleepDataService {


    @Autowired
    private SleepDataMapper sleepDataMapper;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Override
    public List<SleepData> getById(Long userId) {

        return sleepDataMapper.getById(userId);
    }

    @Override
    public List<SleepData> getRecentData(Long userId) {
        return sleepDataMapper.getRecentData(userId, 10);
    }

    @Override
    @Transactional
    public boolean save(SleepData entity) {
        boolean result = super.save(entity);
        if (result) {
            // 发布睡眠数据事件，触发WebSocket推送
            eventPublisher.publishEvent(new SleepDataEvent(this, entity));
        }
        return result;
    }
    @Override
    @Transactional
    public void saveHealthData(SleepData entity) {
        save(entity);
    }

    @Override
    @Transactional
    public void saveFromMQTT(SleepData sleepData, String mac) {
        sleepDataMapper.insertByMac(sleepData.getSleepData(),mac, LocalDateTime.now());
    }
}

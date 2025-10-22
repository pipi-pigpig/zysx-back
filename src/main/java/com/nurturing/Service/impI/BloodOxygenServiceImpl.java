package com.nurturing.Service.impI;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nurturing.Event.BloodOxygenDataEvent;
import com.nurturing.Mapper.BloodOxygenMapper;
import com.nurturing.Service.BloodOxygenService;
import com.nurturing.entity.BloodOxygen;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
public class BloodOxygenServiceImpl extends ServiceImpl<BloodOxygenMapper,BloodOxygen> implements BloodOxygenService {

    @Autowired
    private BloodOxygenMapper bloodOxygenMapper;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Override
    public List<BloodOxygen> getById(Long userId) {
        return bloodOxygenMapper.getById(userId);
    }

    @Override
    public List<BloodOxygen> getRecentData(Long userId) {
        return bloodOxygenMapper.getRecentData(userId, 10);
    }

    @Override
    @Transactional
    public boolean save(BloodOxygen entity) {
        boolean result = super.save(entity);
        if (result) {
            // 发布数据保存事件，触发WebSocket推送
            eventPublisher.publishEvent(new BloodOxygenDataEvent(this, entity));
        }
        return result;
    }

    @Override
    @Transactional
    public boolean saveBatch(Collection<BloodOxygen> entityList) {
        boolean result = super.saveBatch(entityList);
        if (result && !entityList.isEmpty()) {
            // 为每条记录发布事件
            for (BloodOxygen entity : entityList) {
                eventPublisher.publishEvent(new BloodOxygenDataEvent(this, entity));
            }
        }
        return result;
    }

    @Override
    @Transactional
    public void saveFromMQTT(BloodOxygen bloodOxygen, String mac) {
        bloodOxygenMapper.insertByMac(bloodOxygen.getOxygenData(),mac, LocalDateTime.now());
    }
}

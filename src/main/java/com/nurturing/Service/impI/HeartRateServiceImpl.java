package com.nurturing.Service.impI;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nurturing.Event.HeartRateDataEvent;
import com.nurturing.Mapper.HeartRateMapper;
import com.nurturing.Service.HeartRateService;
import com.nurturing.entity.HeartRate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class HeartRateServiceImpl extends ServiceImpl<HeartRateMapper,HeartRate>
        implements HeartRateService {

    @Autowired
    private HeartRateMapper heartRateMapper;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Override
    public List<HeartRate> getById(Long userId) {
        return heartRateMapper.getById(userId);
    }

    @Override
    public List<HeartRate> getRecentData(Long userId) {
        return heartRateMapper.getRecentData(userId, 10);
    }

    @Override
    @Transactional
    public boolean save(HeartRate entity) {
        boolean result = super.save(entity);
        if (result) {
            // 可以创建HeartRateDataEvent并发布事件
             eventPublisher.publishEvent(new HeartRateDataEvent(this, entity));
        }
        return result;
    }

    @Override
    @Transactional
    public void saveHealthData(HeartRate entity) {
        save(entity);
        // 如果需要实时推送，取消上面的注释
         eventPublisher.publishEvent(new HeartRateDataEvent(this, entity));
    }
    @Override
    @Transactional
    public void saveFromMQTT(HeartRate heartRate, String mac) {
        log.info("[HeartRateServiceImpl]"+"HeartData:"+heartRate.getHeartData()+" mac:"+mac);
        heartRateMapper.insertByMac(heartRate.getHeartData(),mac, LocalDateTime.now());
    }
}

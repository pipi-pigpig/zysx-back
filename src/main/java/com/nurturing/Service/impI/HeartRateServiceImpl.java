package com.nurturing.Service.impI;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nurturing.Mapper.HeartRateMapper;
import com.nurturing.Service.HeartRateService;
import com.nurturing.entity.HeartRate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class HeartRateServiceImpl extends ServiceImpl<HeartRateMapper,HeartRate> implements HeartRateService {


    @Autowired
    private HeartRateMapper heartRateMapper;


    @Override
    public List<HeartRate> getById(Long userId) {

        return heartRateMapper.getById(userId);
    }

    @Override
    @Transactional
    public void saveFromMQTT(HeartRate heartRate, String mac) {
        log.info("[HeartRateServiceImpl]"+"HeartData:"+heartRate.getHeartData()+" mac:"+mac);
        heartRateMapper.insertByMac(heartRate.getHeartData(),mac, LocalDateTime.now());
    }


}

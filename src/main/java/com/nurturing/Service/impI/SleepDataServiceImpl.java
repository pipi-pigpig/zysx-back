package com.nurturing.Service.impI;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nurturing.Mapper.SleepDataMapper;
import com.nurturing.Service.SleepDataService;
import com.nurturing.entity.SleepData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class SleepDataServiceImpl extends ServiceImpl<SleepDataMapper,SleepData> implements SleepDataService {


    @Autowired
    private SleepDataMapper sleepDataMapper;


    @Override
    public List<SleepData> getById(Long userId) {

        return sleepDataMapper.getById(userId);
    }

    @Override
    @Transactional
    public void saveFromMQTT(SleepData sleepData, String mac) {
        sleepDataMapper.inserByMac(sleepData.getSleepData(),mac, LocalDateTime.now());
    }
}

package com.nurturing.Service.impI;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nurturing.Mapper.BloodPressureMapper;
import com.nurturing.Service.BloodPressureService;
import com.nurturing.entity.BloodPressure;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class BloodPressureServiceImpl extends ServiceImpl<BloodPressureMapper,BloodPressure> implements BloodPressureService {


    @Autowired
    private BloodPressureMapper bloodPressureMapper;


    @Override
    public List<BloodPressure> getById(Long userId) {
        return bloodPressureMapper.getById(userId);
    }

    @Override
    @Transactional
    public void saveFromMQTT(BloodPressure bloodPressure, String mac) {
        bloodPressureMapper.insertByMac(bloodPressure.getSystolicBp(),bloodPressure.getDiastolicBp(),mac, LocalDateTime.now());
    }
}

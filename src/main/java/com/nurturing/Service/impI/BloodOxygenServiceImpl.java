package com.nurturing.Service.impI;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nurturing.Mapper.BloodOxygenMapper;
import com.nurturing.Service.BloodOxygenService;
import com.nurturing.entity.BloodOxygen;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class BloodOxygenServiceImpl extends ServiceImpl<BloodOxygenMapper,BloodOxygen> implements BloodOxygenService {


    @Autowired
    private BloodOxygenMapper bloodOxygenMapper;


    @Override
    public List<BloodOxygen> getById(Long userId) {

        return bloodOxygenMapper.getById(userId);
    }

    @Override
    @Transactional
    public void saveFromMQTT(BloodOxygen bloodOxygen, String mac) {
        bloodOxygenMapper.insertByMac(bloodOxygen.getOxygenData(),mac, LocalDateTime.now());
    }
}

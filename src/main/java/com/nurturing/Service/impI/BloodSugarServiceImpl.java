package com.nurturing.Service.impI;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nurturing.Mapper.BloodSugarMapper;
import com.nurturing.Service.BloodSugarService;
import com.nurturing.entity.BloodSugar;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class BloodSugarServiceImpl extends ServiceImpl<BloodSugarMapper,BloodSugar> implements BloodSugarService {

    @Autowired
    private BloodSugarMapper bloodSugarMapper;


    @Override
    public List<BloodSugar> getById(Long userId) {

        return bloodSugarMapper.getById(userId);

    }

    @Override
    @Transactional
    public void saveFromMQTT(BloodSugar bloodData, String mac) {
        bloodSugarMapper.insertByMac(bloodData.getBloodData(),mac, LocalDateTime.now());
    }
}

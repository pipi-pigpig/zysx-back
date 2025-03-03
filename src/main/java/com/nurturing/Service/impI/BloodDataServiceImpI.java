package com.nurturing.Service.impI;


import com.nurturing.Mapper.BloodDataMapper;
import com.nurturing.Service.BloodDataService;
import com.nurturing.entity.BloodData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class BloodDataServiceImpI implements BloodDataService {

    @Autowired
    private BloodDataMapper bloodDataMapper;


    @Override
    public List<BloodData> getById(Long userId) {

        return bloodDataMapper.getById(userId);

    }
}

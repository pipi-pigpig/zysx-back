package com.nurturing.Service.impI;


import com.nurturing.Mapper.PressureDataMapper;
import com.nurturing.Service.PressureDataService;
import com.nurturing.entity.PressureData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class PressureDataServiceImpI implements PressureDataService {


    @Autowired
    private PressureDataMapper pressureDataMapper;


    @Override
    public List<PressureData> getById(Long userId) {
        return pressureDataMapper.getById(userId);
    }
}

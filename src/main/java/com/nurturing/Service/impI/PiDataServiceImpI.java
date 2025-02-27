package com.nurturing.Service.impI;

import com.nurturing.Mapper.PiDataMapper;
import com.nurturing.Mapper.PressureDataMapper;
import com.nurturing.Service.PiDataService;
import com.nurturing.entity.PiData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class PiDataServiceImpI implements PiDataService {


    @Autowired
    private PiDataMapper piDataMapper;


    @Override
    public List<PiData> getById(Long userId) {
        return piDataMapper.getById(userId);
    }
}

package com.nurturing.Service.impI;


import com.nurturing.Mapper.HeartDataMapper;
import com.nurturing.Service.HeartDataService;
import com.nurturing.entity.HeartData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class HeartDataServiceImpI implements HeartDataService {


    @Autowired
    private HeartDataMapper heartDataMapper;


    @Override
    public List<HeartData> getById(long userId) {

        List<HeartData> heartData=heartDataMapper.getById(userId);
        return heartData;
    }
}

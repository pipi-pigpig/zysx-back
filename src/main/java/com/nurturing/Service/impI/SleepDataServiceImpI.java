package com.nurturing.Service.impI;


import com.nurturing.Mapper.SleepDataMapper;
import com.nurturing.Service.SleepDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SleepDataServiceImpI implements SleepDataService {


    @Autowired
    private SleepDataMapper sleepDataMapper;


}

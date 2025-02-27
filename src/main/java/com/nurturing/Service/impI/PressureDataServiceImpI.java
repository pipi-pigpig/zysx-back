package com.nurturing.Service.impI;


import com.nurturing.Mapper.PressureDataMapper;
import com.nurturing.Service.PressureDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PressureDataServiceImpI implements PressureDataService {


    @Autowired
    private PressureDataMapper pressureDataMapper;


}

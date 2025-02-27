package com.nurturing.Service.impI;

import com.nurturing.Mapper.PiDataMapper;
import com.nurturing.Mapper.PressureDataMapper;
import com.nurturing.Service.PiDataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PiDataServiceImpI implements PiDataService {


    @Autowired
    private PiDataMapper piDataMapper;


}

package com.nurturing.Service.impI;


import com.nurturing.Mapper.OxygenDataMapper;
import com.nurturing.Service.OxygenDataService;
import com.nurturing.entity.OxygenData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class OxygenDataServiceImpI implements OxygenDataService {


    @Autowired
    private OxygenDataMapper oxygenDataMapper;


    @Override
    public List<OxygenData> getById(long userId) {

        return oxygenDataMapper.getById(userId);
    }
}

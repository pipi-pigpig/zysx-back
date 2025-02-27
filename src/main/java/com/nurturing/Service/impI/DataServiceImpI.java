package com.nurturing.Service.impI;

import com.nurturing.Mapper.DataMapper;
import com.nurturing.Service.DataService;
import com.nurturing.entity.DataVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class DataServiceImpI implements DataService {


    @Autowired
    private DataMapper dataMapper;


    @Override
    public DataVO getByIdWithData(long userId) {
        return null;
    }
}

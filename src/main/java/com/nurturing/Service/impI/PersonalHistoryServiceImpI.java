package com.nurturing.Service.impI;


import com.nurturing.Mapper.PersonalHistoryMapper;
import com.nurturing.Service.PersonalHistoryService;
import com.nurturing.entity.PersonalHistory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class PersonalHistoryServiceImpI implements PersonalHistoryService {


    @Autowired
    private PersonalHistoryMapper personalHistoryMapper;

    @Override
    public PersonalHistory getById(Long userId) {
        return personalHistoryMapper.getById(userId);
    }
}

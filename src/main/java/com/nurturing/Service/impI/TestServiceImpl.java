package com.nurturing.Service.impI;


import com.nurturing.Mapper.TestMapper;
import com.nurturing.Service.TestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TestServiceImpl implements TestService {


    @Autowired
    private TestMapper testMapper;


    @Override
    public String getMessage() {
        return testMapper.getTestMessage();
    }

    @Override
    public void setMessage(String message) {
        testMapper.setTestMessage(message);
    }


}

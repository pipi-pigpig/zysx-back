package com.nurturing.Controller;


import com.nurturing.Service.SleepDataService;
import com.nurturing.Service.TestService;
import com.nurturing.entity.SleepData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@Slf4j
@RestController
//@RequestMapping("/data")
public class TestController {

    @Autowired
    private TestService testService;


    @PostMapping("/getTestMessage")
    public String getMessage() {
        return testService.getMessage();
    }

    @PostMapping("/setTestMessage")
    public String setMessage(@RequestBody String message) {
        testService.setMessage(message);
        return "成功设置message";
    }
}

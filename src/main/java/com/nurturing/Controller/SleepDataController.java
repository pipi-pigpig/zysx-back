package com.nurturing.Controller;


import com.nurturing.Service.SleepDataService;
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
public class SleepDataController {

    @Autowired
    private SleepDataService sleepDataService;


    @PostMapping("/sleepData")
    public List<SleepData> getSleepData(@RequestBody Map<String, Long> request) {

        Long user_id= request.get("user_id");
        log.info("根据id查睡眠: {}", user_id);
        return sleepDataService.getById(user_id);
    }
}

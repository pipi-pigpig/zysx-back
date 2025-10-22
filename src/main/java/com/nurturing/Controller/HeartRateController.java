package com.nurturing.Controller;


import com.nurturing.Service.HeartRateService;
import com.nurturing.entity.HeartRate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@Slf4j
@RestController
//@RequestMapping("/heartData")
public class HeartRateController {

    @Autowired
    private HeartRateService heartRateService;

    @PostMapping("/heartData")
    public List<HeartRate> getHeartData(@RequestBody Map<String, Long> request) {
       Long user_id= request.get("user_id");
        log.info("根据id查心率: {}", user_id);
        return heartRateService.getById(user_id);
    }
}

package com.nurturing.Controller;


import com.nurturing.Service.HeartDataService;
import com.nurturing.entity.HeartData;
import com.nurturing.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/heartData")
public class HeartDataController {

    @Autowired
    private HeartDataService heartDataService;

    @GetMapping("/{user_id}")
    public List<HeartData> getHeartData(@PathVariable long user_id) {

        log.info("根据id查心率: {}", user_id);
        List<HeartData> heartData=heartDataService.getById(user_id);
        return heartData;
    }
}

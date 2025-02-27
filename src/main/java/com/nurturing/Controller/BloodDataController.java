package com.nurturing.Controller;


import com.nurturing.Service.BloodDataService;
import com.nurturing.entity.BloodData;
import com.nurturing.entity.HeartData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/bloodData")
public class BloodDataController {

    @Autowired
    private BloodDataService bloodDataService;


    @GetMapping("/{user_id}")
    public List<BloodData> getBloodData(@PathVariable long user_id) {

        log.info("根据id查血糖：{}",user_id);
        List<BloodData> bloodData=bloodDataService.getById(user_id);
        return bloodData;
    }
}

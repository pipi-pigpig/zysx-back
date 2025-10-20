package com.nurturing.Controller;


import com.nurturing.Service.BloodPressureService;
import com.nurturing.entity.BloodPressure;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@Slf4j
@RestController
//@RequestMapping("/data")
public class BloodPressureController {

    @Autowired
    private BloodPressureService bloodPressureService;

    @PostMapping("/pressureData")
    public List<BloodPressure> getPressureData(@RequestBody Map<String, Long> request) {

        Long user_id= request.get("user_id");
        log.info("根据id查血压: {}", user_id);
        return  bloodPressureService.getById(user_id);
    }
}

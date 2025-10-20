package com.nurturing.Controller;


import com.nurturing.Service.BloodSugarService;
import com.nurturing.entity.BloodSugar;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@Slf4j
@RestController
//@RequestMapping("/bloodData")
public class BloodSugarController {

    @Autowired
    private BloodSugarService bloodSugarService;


//    @GetMapping("/{user_id}")
//    public List<BloodData> getBloodData(@PathVariable long user_id) {
//
//        log.info("根据id查血糖：{}",user_id);
//        List<BloodData> bloodData=bloodSugarService.getById(user_id);
//        return bloodData;
//    }

    @PostMapping("/bloodData")
    public List<BloodSugar> getBloodData(@RequestBody Map<String, Long> request) {
        Long user_id=request.get("user_id");
        log.info("根据id查血糖：{}",user_id);
        return bloodSugarService.getById(user_id);
    }
}

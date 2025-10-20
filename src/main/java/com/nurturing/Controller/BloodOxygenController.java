package com.nurturing.Controller;


import com.nurturing.Service.BloodOxygenService;
import com.nurturing.entity.BloodOxygen;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@Slf4j
@RestController
//@RequestMapping("/oxygenData")
public class BloodOxygenController {

    @Autowired
    private BloodOxygenService bloodOxygenService;

    @PostMapping("/oxygenData")
    public List<BloodOxygen> getOxygenData(@RequestBody Map<String, Long> request) {
        Long user_id= request.get("user_id");
        log.info("根据id查血氧: {}", user_id);
        return bloodOxygenService.getById(user_id);
    }


}

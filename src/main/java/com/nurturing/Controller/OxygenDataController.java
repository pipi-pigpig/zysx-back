package com.nurturing.Controller;


import com.nurturing.Service.OxygenDataService;
import com.nurturing.entity.HeartData;
import com.nurturing.entity.OxygenData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@Slf4j
@RestController
//@RequestMapping("/oxygenData")
public class OxygenDataController {

    @Autowired
    private OxygenDataService oxygenDataService;

    @PostMapping("/oxygenData")
    public List<OxygenData> getOxygenData(@RequestBody Map<String, Long> request) {
        Long user_id= request.get("user_id");
        log.info("根据id查血氧: {}", user_id);
        return oxygenDataService.getById(user_id);
    }


}

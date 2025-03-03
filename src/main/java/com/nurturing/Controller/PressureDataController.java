package com.nurturing.Controller;


import com.nurturing.Service.PressureDataService;
import com.nurturing.entity.PressureData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@Slf4j
@RestController
//@RequestMapping("/data")
public class PressureDataController {

    @Autowired
    private PressureDataService pressureDataService;

    @PostMapping("/pressureData")
    public List<PressureData> getPressureData(@RequestBody Map<String, Long> request) {

        Long user_id= request.get("user_id");
        log.info("根据id查血压: {}", user_id);
        return  pressureDataService.getById(user_id);
    }
}

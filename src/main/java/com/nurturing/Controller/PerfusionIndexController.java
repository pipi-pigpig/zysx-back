package com.nurturing.Controller;


import com.nurturing.Service.PerfusionIndexService;
import com.nurturing.entity.PerfusionIndex;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@Slf4j
@RestController
//@RequestMapping("/data")
public class PerfusionIndexController {

    @Autowired
    private PerfusionIndexService perfusionIndexService;

    @PostMapping("/piData")
    public List<PerfusionIndex> getPiData(@RequestBody Map<String, Long> request) {
        Long user_id= request.get("user_id");
        log.info("根据id查心率: {}", user_id);
        return perfusionIndexService.getById(user_id);
    }
}

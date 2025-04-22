package com.nurturing.Controller;


import com.nurturing.Service.PiDataService;
import com.nurturing.entity.HeartData;
import com.nurturing.entity.MedicalQueryRequest;
import com.nurturing.entity.PiData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin
@Slf4j
@RestController
//@RequestMapping("/data")
public class PiDataController {

    @Autowired
    private PiDataService piDataService;

    @PostMapping("/piData")
    public List<PiData> getPiData(@RequestBody Map<String, Long> request) {
        Long user_id= request.get("user_id");
        log.info("根据id查心率: {}", user_id);
        return piDataService.getById(user_id);
    }

//    @PostMapping("/api/medical/query")
//    public void handleQuery(@RequestBody Map<String, String> request) {
//        String question= request.get("question");
//
//        System.out.println("测试："+question);
//
//
//    }
}

package com.nurturing.Controller;


import com.nurturing.Service.HeartDataService;
import com.nurturing.entity.HeartData;
import com.nurturing.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@CrossOrigin
@Slf4j
@RestController
//@RequestMapping("/heartData")
public class HeartDataController {

    @Autowired
    private HeartDataService heartDataService;
//
//    @PostMapping("/heartData")
//    public List<HeartData> getHeartData(@RequestBody long user_id) {
//
//        log.info("根据id查心率: {}", user_id);
//        List<HeartData> heartData=heartDataService.getById(user_id);
//        return heartData;
//    }

    @PostMapping("/heartData")
    public List<HeartData> getHeartData(@RequestBody Map<String, Long> request) {
       Long user_id= request.get("user_id");
        log.info("根据id查心率: {}", user_id);

        return heartDataService.getById(user_id);
    }

//    @PostMapping("/heartData")
//    public ResponseEntity<?> getHeartData(@RequestBody Map<String, Object> request) {
//        try {
//            Long user_id = Long.parseLong(request.get("user_id").toString());
//            log.info("根据id查心率: {}", user_id);
//            List<HeartData> data = heartDataService.getById(user_id);
//            return ResponseEntity.ok(data);
//        } catch (NumberFormatException e) {
//            log.error("参数格式错误: {}", request);
//            return ResponseEntity.badRequest().body("user_id必须为数字");
//        } catch (Exception e) {
//            log.error("服务器内部错误", e);
//            return ResponseEntity.internalServerError().body("请求失败");
//        }
//    }
}

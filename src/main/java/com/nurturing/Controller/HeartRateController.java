package com.nurturing.Controller;


import com.nurturing.Service.HeartRateService;
import com.nurturing.entity.HeartRate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin
@Slf4j
@RestController
//@RequestMapping("/heartData")
public class HeartRateController {

    @Autowired
    private HeartRateService heartRateService;
//
//    @PostMapping("/heartData")
//    public List<HeartData> getHeartData(@RequestBody long user_id) {
//
//        log.info("根据id查心率: {}", user_id);
//        List<HeartData> heartData=heartRateService.getById(user_id);
//        return heartData;
//    }

    @PostMapping("/heartData")
    public List<HeartRate> getHeartData(@RequestBody Map<String, Long> request) {
       Long user_id= request.get("user_id");
        log.info("根据id查心率: {}", user_id);

        return heartRateService.getById(user_id);
    }

//    @PostMapping("/heartData")
//    public ResponseEntity<?> getHeartData(@RequestBody Map<String, Object> request) {
//        try {
//            Long user_id = Long.parseLong(request.get("user_id").toString());
//            log.info("根据id查心率: {}", user_id);
//            List<HeartData> data = heartRateService.getById(user_id);
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

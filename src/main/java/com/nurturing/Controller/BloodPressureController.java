package com.nurturing.Controller;


import com.nurturing.Service.BloodPressureService;
import com.nurturing.entity.BloodPressure;
import com.nurturing.result.R;
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


    @PostMapping("/api/health-data-aggregated/pressure-data-by-date")
    public R getDailyRecords(@RequestBody Map<String, Object> request) {
        try {
            Long userId = ((Number) request.get("userId")).longValue();
            String date = (String) request.get("date");
            return R.success(bloodPressureService.getDailyRecords(userId, date));
        } catch (Exception e) {
            return R.error(400, "参数错误: " + e.getMessage());
        }
    }

    @PostMapping("/api/health-data-aggregated/pressure-data-by-week")
    public R getWeeklyAverages(@RequestBody Map<String, Object> request) {
        try {
            Long userId = ((Number) request.get("userId")).longValue();
            String dateInWeek = (String) request.get("dateInWeek");
            return R.success(bloodPressureService.getWeeklyAverages(userId, dateInWeek));
        } catch (Exception e) {
            return R.error(400, "参数错误: " + e.getMessage());
        }
    }

    @PostMapping("/api/health-data-aggregated/pressure-data-by-month")
    public R getMonthlyAverages(@RequestBody Map<String, Object> request) {
        try {
            Long userId = ((Number) request.get("userId")).longValue();
            int year = ((Number) request.get("year")).intValue();
            int month = ((Number) request.get("month")).intValue();
            return R.success(bloodPressureService.getMonthlyAverages(userId, year, month));
        } catch (Exception e) {
            return R.error(400, "参数错误: " + e.getMessage());
        }
    }

    @PostMapping("/api/health-data-aggregated/pressure-data-by-year")
    public R getYearlyAverages(@RequestBody Map<String, Object> request) {
        try {
            Long userId = ((Number) request.get("userId")).longValue();
            int year = ((Number) request.get("year")).intValue();
            return R.success(bloodPressureService.getYearlyAverages(userId, year));
        } catch (Exception e) {
            return R.error(400, "参数错误: " + e.getMessage());
        }
    }
}

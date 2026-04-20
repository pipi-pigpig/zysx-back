package com.nurturing.Controller;


import com.nurturing.DTO.*;
import com.nurturing.Service.BloodOxygenService;
import com.nurturing.Service.impI.BloodOxygenAggregatedService;
import com.nurturing.entity.BloodOxygen;
import com.nurturing.result.R;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;

@CrossOrigin
@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
public class BloodOxygenController {

    @Autowired
    private BloodOxygenService bloodOxygenService;

    @PostMapping("/oxygenData")
    public List<BloodOxygen> getOxygenData(@RequestBody Map<String, Long> request) {
        Long user_id= request.get("user_id");
        log.info("根据id查血氧: {}", user_id);
        return bloodOxygenService.getById(user_id);
    }

    private final BloodOxygenAggregatedService bloodOxygenAggregatedService;

    @PostMapping("/api/health-data-aggregated/oxygen-data-by-date")
    public R getOxygenDataByDate(@Valid @RequestBody OxygenDataByDateRequest request) {
        try {
            List<OxygenDataItem> data = bloodOxygenAggregatedService.getOxygenDataByDate(
                    request.getUserId(),
                    request.getDate()
            );
            return R.success(data);
        } catch (Exception e) {
            return R.error(500, "查询血氧数据失败: " + e.getMessage());
        }
    }

    @PostMapping("/api/health-data-aggregated/oxygen-data-by-week")
    public R getOxygenDataByWeek(@Valid @RequestBody OxygenDataByWeekRequest request) {
        try {
            var data = bloodOxygenAggregatedService.getOxygenDataByWeek(request.getUserId(), request.getDateInWeek());
            return R.success(data);
        } catch (DateTimeParseException e) {
            return R.error(400, "无效的日期格式，应为YYYY-MM-DD");
        } catch (Exception e) {
            return R.error(500, "服务器内部错误");
        }
    }

    @PostMapping("/api/health-data-aggregated/oxygen-data-by-month")
    public R getOxygenDataByMonth(@Valid @RequestBody OxygenDataByMonthRequest request) {
        try {
            var data = bloodOxygenAggregatedService.getOxygenDataByMonth(
                    request.getUserId(),
                    request.getYear(),
                    request.getMonth()
            );
            return R.success(data);
        } catch (Exception e) {
            return R.error(500, "服务器内部错误");
        }
    }

    @PostMapping("/api/health-data-aggregated/oxygen-data-by-year")
    public R getOxygenDataByYear(@Valid @RequestBody OxygenDataByYearRequest request) {
        try {
            var data = bloodOxygenAggregatedService.getOxygenDataByYear(request.getUserId(), request.getYear());
            return R.success(data);
        } catch (Exception e) {
            return R.error(500, "服务器内部错误");
        }
    }
}

package com.nurturing.Controller;


import com.nurturing.DTO.SleepDataByDateRequest;
import com.nurturing.DTO.SleepDataByMonthRequest;
import com.nurturing.DTO.SleepDataByWeekRequest;
import com.nurturing.DTO.SleepDataByYearRequest;
import com.nurturing.Service.SleepDataService;
import com.nurturing.Service.impI.SleepDataAggregatedService;
import com.nurturing.entity.SleepData;
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
public class SleepDataController {

    @Autowired
    private SleepDataService sleepDataService;

    private final SleepDataAggregatedService sleepDataAggregatedService;

    @PostMapping("/sleepData")
    public List<SleepData> getSleepData(@RequestBody Map<String, Long> request) {
        Long user_id= request.get("user_id");
        log.info("根据id查睡眠: {}", user_id);
        return sleepDataService.getById(user_id);
    }

    @PostMapping("/api/health-data-aggregated/sleep-data-by-date")
    public R getSleepDataByDate(@Valid @RequestBody SleepDataByDateRequest request) {
        try {
            var data = sleepDataAggregatedService.getSleepDataByDate(
                    request.getUserId(),
                    request.getDate()
            );
            return R.success(data);
        } catch (Exception e) {
            return R.error(500, "查询睡眠数据失败: " + e.getMessage());
        }
    }

    @PostMapping("/api/health-data-aggregated/sleep-data-by-week")
    public R getSleepDataByWeek(@Valid @RequestBody SleepDataByWeekRequest request) {
        try {
            var data = sleepDataAggregatedService.getSleepDataByWeek(request.getUserId(), request.getDateInWeek());
            return R.success(data);
        } catch (DateTimeParseException e) {
            return R.error(400, "无效的日期格式，应为YYYY-MM-DD");
        } catch (Exception e) {
            return R.error(500, "服务器内部错误");
        }
    }

    @PostMapping("/api/health-data-aggregated/sleep-data-by-month")
    public R getSleepDataByMonth(@Valid @RequestBody SleepDataByMonthRequest request) {
        try {
            var data = sleepDataAggregatedService.getSleepDataByMonth(
                    request.getUserId(),
                    request.getYear(),
                    request.getMonth()
            );
            return R.success(data);
        } catch (Exception e) {
            return R.error(500, "服务器内部错误");
        }
    }

    @PostMapping("/api/health-data-aggregated/sleep-data-by-year")
    public R getSleepDataByYear(@Valid @RequestBody SleepDataByYearRequest request) {
        try {
            var data = sleepDataAggregatedService.getSleepDataByYear(request.getUserId(), request.getYear());
            return R.success(data);
        } catch (Exception e) {
            return R.error(500, "服务器内部错误");
        }
    }
}

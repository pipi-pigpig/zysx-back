package com.nurturing.Controller;


import com.nurturing.DTO.*;
import com.nurturing.Service.HeartRateService;
import com.nurturing.Service.impI.HeartRateAggregatedService;
import com.nurturing.entity.HeartDataItem;
import com.nurturing.entity.HeartRate;
import com.nurturing.result.R;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin
@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
//@RequestMapping("/heartData")
public class HeartRateController {

    @Autowired
    private HeartRateService heartRateService;

    @PostMapping("/heartData")
    public List<HeartRate> getHeartData(@RequestBody Map<String, Long> request) {
       Long user_id= request.get("user_id");
        log.info("根据id查心率: {}", user_id);
        return heartRateService.getById(user_id);
    }

    private final HeartRateAggregatedService heartRateAggregatedService;

    /**
     * 1. 单日心率查询
     */
    @PostMapping("/api/health-data-aggregated/heart-data-by-date")
    public R getHeartDataByDate(@Valid @RequestBody HeartDataByDateRequest request) {
        try {
            List<HeartDataItem> data = heartRateAggregatedService.getHeartDataByDate(
                    request.getUserId(),
                    request.getDate()
            );
            return R.success(data);
        } catch (Exception e) {
            return R.error(500, "查询心率数据失败: " + e.getMessage());
        }
    }

    /**
     * 2. 周平均心率查询
     */
    @PostMapping("/api/health-data-aggregated/heart-data-by-week")
    public R getHeartDataByWeek(@Valid @RequestBody HeartDataByWeekRequest request) {
        try {
            var data = heartRateAggregatedService.getHeartDataByWeek(request.getUserId(), request.getDateInWeek());
            return R.success(data);
        } catch (DateTimeParseException e) {
            return R.error(400, "无效的日期格式，应为YYYY-MM-DD");
        } catch (Exception e) {
            return R.error(500, "服务器内部错误");
        }
    }

    /**
     * 3. 月平均心率查询
     */
    @PostMapping("/api/health-data-aggregated/heart-data-by-month")
    public R getHeartDataByMonth(@Valid @RequestBody HeartDataByMonthRequest request) {
        try {
            var data = heartRateAggregatedService.getHeartDataByMonth(
                    request.getUserId(),
                    request.getYear(),
                    request.getMonth()
            );
            return R.success(data);
        } catch (Exception e) {
            return R.error(500, "服务器内部错误");
        }
    }

    /**
     * 4. 年平均心率查询
     */
    @PostMapping("/api/health-data-aggregated/heart-data-by-year")
    public R getHeartDataByYear(@Valid @RequestBody HeartDataByYearRequest request) {
        try {
            var data = heartRateAggregatedService.getHeartDataByYear(request.getUserId(), request.getYear());
            return R.success(data);
        } catch (Exception e) {
            return R.error(500, "服务器内部错误");
        }
    }
}

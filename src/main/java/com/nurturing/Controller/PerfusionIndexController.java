package com.nurturing.Controller;


import com.nurturing.DTO.*;
import com.nurturing.Service.PerfusionIndexService;
import com.nurturing.Service.impI.PerfusionIndexAggregatedService;
import com.nurturing.entity.PerfusionIndex;
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
public class PerfusionIndexController {

    @Autowired
    private PerfusionIndexService perfusionIndexService;

    private final PerfusionIndexAggregatedService perfusionIndexAggregatedService;

    @PostMapping("/piData")
    public List<PerfusionIndex> getPiData(@RequestBody Map<String, Long> request) {
        Long user_id= request.get("user_id");
        log.info("根据id查灌注指数: {}", user_id);
        return perfusionIndexService.getById(user_id);
    }

    @PostMapping("/api/health-data-aggregated/pi-data-by-date")
    public R getPiDataByDate(@Valid @RequestBody PiDataByDateRequest request) {
        try {
            List<PiDataItem> data = perfusionIndexAggregatedService.getPiDataByDate(
                    request.getUserId(),
                    request.getDate()
            );
            return R.success(data);
        } catch (Exception e) {
            return R.error(500, "查询灌注指数数据失败: " + e.getMessage());
        }
    }

    @PostMapping("/api/health-data-aggregated/pi-data-by-week")
    public R getPiDataByWeek(@Valid @RequestBody PiDataByWeekRequest request) {
        try {
            var data = perfusionIndexAggregatedService.getPiDataByWeek(request.getUserId(), request.getDateInWeek());
            return R.success(data);
        } catch (DateTimeParseException e) {
            return R.error(400, "无效的日期格式，应为YYYY-MM-DD");
        } catch (Exception e) {
            return R.error(500, "服务器内部错误");
        }
    }

    @PostMapping("/api/health-data-aggregated/pi-data-by-month")
    public R getPiDataByMonth(@Valid @RequestBody PiDataByMonthRequest request) {
        try {
            var data = perfusionIndexAggregatedService.getPiDataByMonth(
                    request.getUserId(),
                    request.getYear(),
                    request.getMonth()
            );
            return R.success(data);
        } catch (Exception e) {
            return R.error(500, "服务器内部错误");
        }
    }

    @PostMapping("/api/health-data-aggregated/pi-data-by-year")
    public R getPiDataByYear(@Valid @RequestBody PiDataByYearRequest request) {
        try {
            var data = perfusionIndexAggregatedService.getPiDataByYear(request.getUserId(), request.getYear());
            return R.success(data);
        } catch (Exception e) {
            return R.error(500, "服务器内部错误");
        }
    }
}

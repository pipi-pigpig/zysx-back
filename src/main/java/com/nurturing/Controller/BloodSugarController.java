package com.nurturing.Controller;


import com.nurturing.DTO.BloodDataByDateRequest;
import com.nurturing.DTO.BloodDataByDateResponse;
import com.nurturing.DTO.BloodDataByWeekRequest;
import com.nurturing.Mapper.BloodSugarMapper;
import com.nurturing.Service.BloodSugarService;
import com.nurturing.entity.BloodSugar;
import com.nurturing.entity.BloodSugarRecord;
import com.nurturing.result.R;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin
@Slf4j
@RestController
//@RequestMapping("/bloodData")
@RequiredArgsConstructor
@Validated
public class BloodSugarController {

    @Autowired
    private BloodSugarService bloodSugarService;


//    @GetMapping("/{user_id}")
//    public List<BloodData> getBloodData(@PathVariable long user_id) {
//
//        log.info("根据id查血糖：{}",user_id);
//        List<BloodData> bloodData=bloodSugarService.getById(user_id);
//        return bloodData;
//    }

    @PostMapping("/bloodData")
    public List<BloodSugar> getBloodData(@RequestBody Map<String, Long> request) {
        Long user_id=request.get("user_id");
        log.info("根据id查血糖：{}",user_id);
        return bloodSugarService.getById(user_id);
    }


    private final BloodSugarMapper bloodSugarMapper;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @PostMapping("/api/health-data-aggregated/blood-data-by-date")
    public ResponseEntity<BloodDataByDateResponse> getBloodDataByDate(
            @Valid @RequestBody BloodDataByDateRequest request) {

        try {
            // 1. 严格验证日期格式
            LocalDate date = LocalDate.parse(request.getDate(), DATE_FORMATTER);

            // 2. 查询数据库 (使用实际表结构字段)
            List<BloodSugarRecord> records = bloodSugarMapper.selectByUserAndDate(
                    request.getUserId(),
                    date
            );

            // 3. 构建响应 (严格匹配接口字段名)
            BloodDataByDateResponse response = new BloodDataByDateResponse();
            List<BloodDataByDateResponse.BloodRecordItem> items = records.stream()
                    .map(record -> {
                        BloodDataByDateResponse.BloodRecordItem item = new BloodDataByDateResponse.BloodRecordItem();
                        item.setBloodData(record.getBloodSugarValue());  // blood_sugar_value -> bloodData
                        item.setRecordTime(record.getRecordTime());      // record_time -> recordTime
                        return item;
                    })
                    .collect(Collectors.toList());

            response.setData(items);
            return ResponseEntity.ok(response);

        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body(createErrorResponse(400, "无效的日期格式，应为YYYY-MM-DD"));
        } catch (Exception e) {
            // 实际项目中应记录详细错误日志
            return ResponseEntity.internalServerError().body(createErrorResponse(500, "服务器内部错误"));
        }
    }

    private BloodDataByDateResponse createErrorResponse(int code, String message) {
        BloodDataByDateResponse response = new BloodDataByDateResponse();
        response.setCode(code);
        response.setMessage(message);
        return response;
    }

    @PostMapping("/api/health-data-aggregated/blood-data-by-week")
    public R getBloodDataByWeek(@Valid @RequestBody BloodDataByWeekRequest request) {
        try {
            List<?> data = bloodSugarService.getBloodDataByWeek(request.getUserId(), request.getDateInWeek());
            return R.success(data);
        } catch (DateTimeParseException e) {
            return R.error(400, "无效的日期格式，应为YYYY-MM-DD");
        } catch (Exception e) {
            // 实际项目中应记录日志
            return R.error(500, "服务器内部错误");
        }
    }
}

package com.nurturing.Controller;

import com.nurturing.DTO.ChatHistoryUpdate;
import com.nurturing.DTO.ChatRequest;
import com.nurturing.DTO.GetHealthReportListResponse;
import com.nurturing.DTO.SaveHealthReportRequest;
import com.nurturing.Service.ChatService;
import com.nurturing.Service.HealthReportService;
import com.nurturing.entity.HealthReport;
import com.nurturing.result.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@CrossOrigin
@Slf4j
@RestController
@RequestMapping("/api")
public class MedicalController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private HealthReportService healthReportService;

    @PostMapping("/query/stream")
    public SseEmitter queryStream(@RequestBody ChatRequest request) throws IOException {

        String sessionId = request.getSessionId();

        return chatService.queryStream(sessionId,request.getQuestion());
    }

    @PostMapping("/query/history/update")
    public R updateHistory(@RequestBody ChatHistoryUpdate historyUpdate){
        try{
            chatService.setQuestionHistory(historyUpdate.getSessionId(),historyUpdate.getQuestion());
            chatService.setAnswerHistory(historyUpdate.getSessionId(),historyUpdate.getAnswer());
            return R.success();
        }catch (Exception e){
            return R.error(500,"服务器内部错误");
        }
    }

    @DeleteMapping("/query/history/clean")
    public R cleanHistory(@RequestBody String sessionId){

        try{
            chatService.cleanHistory(sessionId);
            return R.success();
        }catch (Exception e){
            return R.error(500,"服务器内部错误");
        }

    }


    @PostMapping("/healthReport/generate")
    public SseEmitter generateHealthReport(@RequestParam Long userId) throws IOException {

        return healthReportService.generateHealthReport(userId);
    }

    @PostMapping("/healthReport/save")
    public R saveHealthReport(@RequestBody SaveHealthReportRequest saveHealthReportRequest){
        try{
            HealthReport healthReport= new HealthReport();
            healthReport.setUserId(saveHealthReportRequest.getUserId());
            healthReport.setCreateTime(LocalDateTime.now());
            healthReport.setReport(saveHealthReportRequest.getReport());
            healthReportService.saveHealthReport(healthReport);
            return R.success();
        }catch (Exception e){
            return R.error(500,"服务器内部错误");
        }
    }

    @GetMapping("/healthReport")
    public R getHealthReport(@RequestParam Long healthReportId){
        try{
            return R.success(healthReportService.getHealthReport(healthReportId));
        }catch (Exception e){
            return R.error(500,"服务器内部错误");
        }
    }

    @GetMapping("/healthReport/list")
    public R getHealthReportList(@RequestParam Long userId){
        try{

            return R.success(healthReportService.getHealthReportList(userId));
        }catch (Exception e){
            return R.error(500,"服务器内部错误");
        }
    }

    @DeleteMapping("/healthReport")
    public R deleteHealthReport(@RequestParam Long healthReportId){

        try{
            healthReportService.deleteHealthReport(healthReportId);
            return R.success();
        }catch (Exception e){
            return R.error(500,"服务器内部错误");
        }

    }

}

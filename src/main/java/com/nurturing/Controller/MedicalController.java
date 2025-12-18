package com.nurturing.Controller;

import com.nurturing.DTO.ChatHistoryUpdate;
import com.nurturing.DTO.ChatRequest;
import com.nurturing.DTO.ChatResponse;
import com.nurturing.Service.ChatService;
import com.nurturing.Service.impI.ChatServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.UUID;


@CrossOrigin
@Slf4j
@RestController
//@RequestMapping("/api/medical")
public class MedicalController {

    @Autowired
    private ChatService chatService;

    @PostMapping("/query/stream")
    public SseEmitter queryStream(@RequestBody ChatRequest request) throws IOException {

        String sessionId = request.getSessionId();

        return chatService.queryStream(sessionId,request.getQuestion());
    }

    @PostMapping("/query/history/update")
    public void updateHistory(@RequestBody ChatHistoryUpdate historyUpdate) throws IOException {
        chatService.setQuestionHistory(historyUpdate.getSessionId(),historyUpdate.getQuestion());
        chatService.setAnswerHistory(historyUpdate.getSessionId(),historyUpdate.getAnswer());
    }

    @DeleteMapping("/query/history/clean")
    public void cleanHistory(@RequestBody String sessionId){
        chatService.cleanHistory(sessionId);
    }
}

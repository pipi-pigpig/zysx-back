package com.nurturing.Controller;

import com.nurturing.entity.MedicalQueryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


    @CrossOrigin
    @RestController
    @RequestMapping("/api/medical")
    public class MedicalQaController {

        @Autowired
        private FastAPIClient fastAPIClient; // 封装FastAPI调用

        @PostMapping("/query")
        public ResponseEntity<Map<String, Object>> handleQuery(@RequestBody Map<String, String> request) {

            //System.out.println("打印问题 "+request.getQuestion());

//            String question= request.getQuestion();


            String question= request.get("question");
            // 1. 转发请求到FastAPI服务
            Map<String, String> fastApiResponse = fastAPIClient.queryMedicalQA(question);

            // 2. 构造返回结果
            Map<String, Object> response = new HashMap<>();
           // response.put("intent", fastApiResponse.get("intent"));
            response.put("answer", fastApiResponse.get("answer"));
            response.put("timestamp", LocalDateTime.now());

            return ResponseEntity.ok(response);
        }
    }

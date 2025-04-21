package com.nurturing.Controller;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


    @Component
    public class FastAPIClient {

        @Value("${fastapi.url}")
        private String fastApiUrl;

        public Map<String, String> queryMedicalQA(String question) {
            RestTemplate restTemplate = new RestTemplate();

            // 1. 构造FastAPI请求体
            Map<String, String> request = new HashMap<>();
            request.put("text", question);

            // 2. 发送POST请求
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    fastApiUrl + "/query",
                    request,
                    Map.class
            );

            // 3. 提取响应数据
            return (Map<String, String>) response.getBody();
        }
    }


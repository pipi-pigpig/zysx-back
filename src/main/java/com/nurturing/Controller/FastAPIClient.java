package com.nurturing.Controller;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;


    @Component
    public class FastAPIClient {

        @Value("${fastapi.url}")
        private String fastApiUrl;

        public Map<String, String> queryMedicalQA(String question) {
            RestTemplate restTemplate = new RestTemplate();

            // 设置请求头
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON); // 关键设置

            // 构造请求体
            Map<String, String> request = new HashMap<>();
            request.put("text", question);

            // 封装请求体和头
            HttpEntity<Map<String, String>> entity = new HttpEntity<>(request, headers);

            // 发送POST请求
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    fastApiUrl + "/query",
                    entity,  // 传递封装后的实体
                    Map.class
            );

            return (Map<String, String>) response.getBody();
        }
    }


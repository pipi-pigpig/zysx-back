package com.nurturing.Service.impI;

import com.nurturing.DTO.MedicalQueryInput;
import com.nurturing.DTO.QueryResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.RestClientException;

@Service
public class MedicalQAService {
    private final RestTemplate restTemplate = new RestTemplate();
    private final String apiUrl = "http://120.26.14.104:8000/query"; // 替换实际IP

    public String queryMedicalQA(String question) {
        try {
            MedicalQueryInput request = new MedicalQueryInput();
            request.setText(question);

            // 发送请求时显式指定Content-Type
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<MedicalQueryInput> entity = new HttpEntity<>(request, headers);

            ResponseEntity<QueryResponse> response = restTemplate.postForEntity(
                    apiUrl,
                    entity,  // 使用HttpEntity包装请求体和Header
                    QueryResponse.class
            );

            return response.getBody().getAnswer();
        } catch (RestClientException e) {
            throw new RuntimeException("调用失败: " + e.getMessage());
        }
    }
}

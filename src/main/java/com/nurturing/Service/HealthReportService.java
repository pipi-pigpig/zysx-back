package com.nurturing.Service;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

public interface HealthReportService {
    SseEmitter generateHealthReport(Long userId) throws IOException;

    String getHealthReport(String sessionId);

    void saveHealthReport(String sessionId, String healthReport);
}

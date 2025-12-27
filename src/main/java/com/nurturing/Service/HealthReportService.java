package com.nurturing.Service;

import com.nurturing.DTO.GetHealthReportListResponse;
import com.nurturing.entity.HealthReport;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;

public interface HealthReportService {
    SseEmitter generateHealthReport(Long userId) throws IOException;

    List<GetHealthReportListResponse> getHealthReportList(Long userId);

    HealthReport getHealthReport(Long healthReportId);

    void saveHealthReport(HealthReport healthReport);

    void deleteHealthReport(Long healthReportId);
}

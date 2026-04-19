package com.nurturing.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportRequest {
    private String request_id;
    private String timestamp;
    private String user_id;
    private ClientInfo client_info;
    private ReportBody body;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClientInfo {
        private String client_type;
        private String version;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReportBody {
        private String task_id;
        private MonitoringData monitoring_data;
        private UserProfile user_profile;
        private String session_id;
    }
}

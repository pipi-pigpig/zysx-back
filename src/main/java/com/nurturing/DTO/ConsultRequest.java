package com.nurturing.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConsultRequest {
    private String request_id;
    private String timestamp;
    private String user_id;
    private ClientInfo client_info;
    private ConsultBody body;

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
    public static class ConsultBody {
        private String task_id;
        private List<ChatMessage> chat_history;
        private String question;
        private String session_id;
        private List<ChatMessage> conversation_history;
        private UserProfile user_profile;
        private Map<String, Object> context;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ChatMessage {
        private String role;
        private String content;
    }
}

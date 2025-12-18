package com.nurturing.DTO;

import lombok.Data;

@Data
public class ChatHistoryUpdate {
    private String sessionId;
    private String question;
    private String answer;
}

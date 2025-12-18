package com.nurturing.DTO;

import com.nurturing.entity.ChatSentence;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class ChatModelRequest {
    private String model = "Qwen3-4B-Instruct-2507";
    private List<ChatSentence> messages;
    private boolean stream = true; // 根据您的需求，通常流式请求需要设置为 true
    private double temperature = 0.1; // 默认值
    private int max_tokens = 1024; // 默认值

    public ChatModelRequest(List<ChatSentence> messages) {
        this.messages = messages;
    }
}

package com.nurturing.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class ChatSentence implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L; // 序列化版本号

    private String role;
    private String content;

    public ChatSentence(String role, String content) {
        this.role = role;
        this.content = content;
    }
}

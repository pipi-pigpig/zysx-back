package com.nurturing.entity;

import lombok.Data;

@Data
public class MedicalQueryRequest {
    private String question;
    //private String userId; // 可选用户ID
}

package com.nurturing.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicalQueryRequest {
    private String question;
    //private String userId; // 可选用户ID
}

package com.nurturing.entity;

import lombok.Data;

@Data
public class FastAPIResponse {
    private String intent;
    private String answer;
    private Double confidence;
}

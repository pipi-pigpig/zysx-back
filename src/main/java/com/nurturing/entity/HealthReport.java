package com.nurturing.entity;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HealthReport {
    Long id;
    Long userId;
    LocalDateTime createTime;
    String report;
}

package com.nurturing.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HealthData {
    private Integer id;
    private int spo2;
    private int bmp;
    private int pi;
    private String deviceCode;
    private String mac;
    private LocalDateTime recordTime;

}
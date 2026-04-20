package com.nurturing.DTO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OxygenWeekDayData {
    private String date;
    private BigDecimal avgOxygen;
}

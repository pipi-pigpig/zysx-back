package com.nurturing.DTO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OxygenMonthWeekData {
    private Integer week;
    private BigDecimal avgOxygen;
}

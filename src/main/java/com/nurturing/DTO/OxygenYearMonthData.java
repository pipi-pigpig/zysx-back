package com.nurturing.DTO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OxygenYearMonthData {
    private Integer month;
    private BigDecimal avgOxygen;
}

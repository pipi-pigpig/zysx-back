package com.nurturing.DTO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PiMonthWeekData {
    private Integer week;
    private BigDecimal avgPi;
}

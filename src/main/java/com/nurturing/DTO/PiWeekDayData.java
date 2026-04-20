package com.nurturing.DTO;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PiWeekDayData {
    private String date;
    private BigDecimal avgPi;
}

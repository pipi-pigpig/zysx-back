package com.nurturing.DTO;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author ZhangQinAn
 * @email 242646968@qq.com
 * @since 2025/12/10 上午11:44
 */
@Data
public class WeekDayData {
    private String date;       // YYYY-MM-DD
    private BigDecimal avgValue;
}

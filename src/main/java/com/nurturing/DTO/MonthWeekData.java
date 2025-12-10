package com.nurturing.DTO;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author ZhangQinAn
 * @email 242646968@qq.com
 * @since 2025/12/10 下午5:46
 */
@Data
public class MonthWeekData {
    private String weekStart;
    private String weekEnd;
    private BigDecimal avgValue;
}

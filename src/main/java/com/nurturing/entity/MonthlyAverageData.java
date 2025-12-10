package com.nurturing.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author ZhangQinAn
 * @email 242646968@qq.com
 * @since 2025/12/10 下午5:37
 */
@Data
public class MonthlyAverageData {
    private Long id;
    private Long userId;
    private String dataType;
    private BigDecimal averageValue;
    private LocalDate monthDate; // 对应 month_date
}
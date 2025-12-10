package com.nurturing.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @author ZhangQinAn
 * @email 242646968@qq.com
 * @since 2025/12/10 上午11:40
 */
@Data
public class DailyAverageData {
    private Long id;
    private Long userId;
    private String dataType;      // blood_sugar
    private BigDecimal averageValue;
    private LocalDate recordDate;
}
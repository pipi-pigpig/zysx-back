package com.nurturing.DTO;

import lombok.Data;

/**
 * @author ZhangQinAn
 * @email 242646968@qq.com
 * @since 2025/12/18 上午9:02
 */
@Data
public class BloodPressureMonthlyAverage {
    private String weekStart; // "2025-05-05"
    private String weekEnd;   // "2025-05-11"
    private Double avgSystolic;
    private Double avgDiastolic;
}

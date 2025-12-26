package com.nurturing.DTO;

import lombok.Data;

/**
 * @author ZhangQinAn
 * @email 242646968@qq.com
 * @since 2025/12/18 上午9:03
 */
@Data
public class BloodPressureYearlyAverage {
    private String month; // "2025-01"
    private Double avgSystolic;
    private Double avgDiastolic;
}

package com.nurturing.DTO;

import lombok.Data;

/**
 * @author ZhangQinAn
 * @email 242646968@qq.com
 * @since 2025/12/18 上午9:02
 */
@Data
public class BloodPressureWeeklyAverage {
    private String date; // "2025-05-12"
    private Double avgSystolic;
    private Double avgDiastolic;
}

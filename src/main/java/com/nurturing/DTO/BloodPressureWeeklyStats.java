package com.nurturing.DTO;

import lombok.Data;

@Data
public class BloodPressureWeeklyStats {
    private String week;
    private Double avgSystolic;
    private Double avgDiastolic;
}

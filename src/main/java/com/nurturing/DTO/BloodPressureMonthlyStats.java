package com.nurturing.DTO;

import lombok.Data;

@Data
public class BloodPressureMonthlyStats {
    private String month;
    private Double avgSystolic;
    private Double avgDiastolic;
}

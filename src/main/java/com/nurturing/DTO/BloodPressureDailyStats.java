package com.nurturing.DTO;

import lombok.Data;

@Data
public class BloodPressureDailyStats {
    private String date;
    private Double avgSystolic;
    private Double avgDiastolic;
}

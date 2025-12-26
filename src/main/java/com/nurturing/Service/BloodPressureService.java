package com.nurturing.Service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.nurturing.DTO.BloodPressureDailyRecord;
import com.nurturing.DTO.BloodPressureMonthlyAverage;
import com.nurturing.DTO.BloodPressureWeeklyAverage;
import com.nurturing.DTO.BloodPressureYearlyAverage;
import com.nurturing.entity.BloodPressure;

import java.util.List;

public interface BloodPressureService extends HealthDataService<BloodPressure> {
    List<BloodPressure> getById(Long userId);

    void saveFromMQTT(BloodPressure bloodPressure,String mac);

    List<BloodPressureDailyRecord> getDailyRecords(Long userId, String date);

    List<BloodPressureWeeklyAverage> getWeeklyAverages(Long userId, String dateInWeek);

    List<BloodPressureMonthlyAverage> getMonthlyAverages(Long userId, int year, int month);

    List<BloodPressureYearlyAverage> getYearlyAverages(Long userId, int year);
}

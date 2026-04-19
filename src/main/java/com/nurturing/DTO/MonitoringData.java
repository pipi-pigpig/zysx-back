package com.nurturing.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonitoringData {
    private HeartRateData heart_rate;
    private BloodGlucoseData blood_glucose;
    private PerfusionIndexData perfusion_index;
    private BloodOxygenData blood_oxygen;
    private SleepData sleep;
    private BloodPressureData blood_pressure;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class HeartRateData {
        private List<Object> latest;
        private List<Object> daily_stats;
        private List<Object> weekly_stats;
        private List<Object> monthly_stats;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BloodGlucoseData {
        private List<Object> latest;
        private List<Object> daily_stats;
        private List<Object> weekly_stats;
        private List<Object> monthly_stats;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PerfusionIndexData {
        private List<Object> latest;
        private List<Object> daily_stats;
        private List<Object> weekly_stats;
        private List<Object> monthly_stats;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BloodOxygenData {
        private List<Object> latest;
        private List<Object> daily_stats;
        private List<Object> weekly_stats;
        private List<Object> monthly_stats;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SleepData {
        private List<Object> latest;
        private List<Object> daily_stats;
        private List<Object> weekly_stats;
        private List<Object> monthly_stats;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BloodPressureData {
        private List<Object> latest;
        private List<Object> daily_stats;
        private List<Object> weekly_stats;
        private List<Object> monthly_stats;
    }
}

package com.nurturing.Service.impI;

import com.nurturing.DTO.BloodPressureDailyStats;
import com.nurturing.DTO.BloodPressureMonthlyStats;
import com.nurturing.DTO.BloodPressureWeeklyStats;
import com.nurturing.DTO.GetHealthReportListResponse;
import com.nurturing.DTO.MonitoringData;
import com.nurturing.DTO.UserProfile;
import com.nurturing.Mapper.*;
import com.nurturing.Service.HealthReportService;
import com.nurturing.Service.UserLoginService;
import com.nurturing.chat.LLMClient;
import com.nurturing.entity.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class HealthReportServiceImpl implements HealthReportService {

    private static final Logger log = LoggerFactory.getLogger(HealthReportServiceImpl.class);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    private LLMClient llmClient;

    @Autowired
    private UserLoginService userLoginService;

    @Autowired
    private DailyAverageDataMapper dailyAverageDataMapper;

    @Autowired
    private WeeklyAverageDataMapper weeklyAverageDataMapper;

    @Autowired
    private MonthlyAverageDataMapper monthlyAverageDataMapper;

    @Autowired
    private HealthReportMapper healthReportMapper;

    @Autowired
    private HeartRateMapper heartRateMapper;

    @Autowired
    private BloodSugarMapper bloodSugarMapper;

    @Autowired
    private BloodPressureMapper bloodPressureMapper;

    @Autowired
    private BloodOxygenMapper bloodOxygenMapper;

    @Autowired
    private PerfusionIndexMapper perfusionIndexMapper;

    @Autowired
    private SleepDataMapper sleepDataMapper;

    @Override
    public SseEmitter generateHealthReport(Long userId) throws IOException {
        String sessionId = "report:" + UUID.randomUUID().toString();
        log.info("开始生成健康报告, userId: {}, sessionId: {}", userId, sessionId);

        User user = userLoginService.getUserById(userId);
        if (user == null) {
            log.error("用户不存在, userId: {}", userId);
            throw new IOException("用户不存在");
        }

        MonitoringData monitoringData = collectMonitoringData(userId);
        UserProfile userProfile = buildUserProfile(user);

        log.info("监测数据收集完成, userId: {}", userId);
        return llmClient.streamGenerateHealthReport(sessionId, null, monitoringData, userProfile, userId.toString());
    }

    private MonitoringData collectMonitoringData(Long userId) {
        MonitoringData monitoringData = new MonitoringData();

        monitoringData.setHeart_rate(collectHeartRateData(userId));
        monitoringData.setBlood_glucose(collectBloodGlucoseData(userId));
        monitoringData.setPerfusion_index(collectPerfusionIndexData(userId));
        monitoringData.setBlood_oxygen(collectBloodOxygenData(userId));
        monitoringData.setSleep(collectSleepData(userId));
        monitoringData.setBlood_pressure(collectBloodPressureData(userId));

        return monitoringData;
    }

    private MonitoringData.HeartRateData collectHeartRateData(Long userId) {
        List<Object> latest = new ArrayList<>();
        List<HeartRate> latestRecords = heartRateMapper.getRecentData(userId, 5);
        for (HeartRate hr : latestRecords) {
            Map<String, Object> item = new HashMap<>();
            item.put("value", hr.getHeartData() != null ? hr.getHeartData().intValue() : null);
            item.put("unit", "bpm");
            item.put("time", hr.getRecordTime() != null ? hr.getRecordTime().format(DATETIME_FORMATTER) : null);
            latest.add(item);
        }

        List<Object> dailyStats = new ArrayList<>();
        List<DailyAverageData> dailyData = dailyAverageDataMapper.selectByLatestTimes(userId, "heart_rate", 30);
        for (DailyAverageData dad : dailyData) {
            Map<String, Object> item = new HashMap<>();
            item.put("date", dad.getRecordDate() != null ? dad.getRecordDate().format(DATE_FORMATTER) : null);
            item.put("avg", dad.getAverageValue() != null ? dad.getAverageValue().intValue() : null);
            dailyStats.add(item);
        }

        List<Object> weeklyStats = new ArrayList<>();
        List<WeeklyAverageData> weeklyData = weeklyAverageDataMapper.selectByLatestTimes(userId, "heart_rate", 12);
        for (WeeklyAverageData wad : weeklyData) {
            Map<String, Object> item = new HashMap<>();
            item.put("week", wad.getWeekStartDate() != null ? wad.getWeekStartDate().format(DATE_FORMATTER) : null);
            item.put("avg", wad.getAverageValue() != null ? wad.getAverageValue().intValue() : null);
            weeklyStats.add(item);
        }

        List<Object> monthlyStats = new ArrayList<>();
        List<MonthlyAverageData> monthlyData = monthlyAverageDataMapper.selectByLatestTimes(userId, "heart_rate", 6);
        for (MonthlyAverageData mad : monthlyData) {
            Map<String, Object> item = new HashMap<>();
            item.put("month", mad.getMonthDate() != null ? mad.getMonthDate().format(DATE_FORMATTER) : null);
            item.put("avg", mad.getAverageValue() != null ? mad.getAverageValue().intValue() : null);
            monthlyStats.add(item);
        }

        return new MonitoringData.HeartRateData(latest, dailyStats, weeklyStats, monthlyStats);
    }

    private MonitoringData.BloodGlucoseData collectBloodGlucoseData(Long userId) {
        List<Object> latest = new ArrayList<>();
        List<BloodSugar> latestRecords = bloodSugarMapper.getRecentData(userId, 5);
        for (BloodSugar bs : latestRecords) {
            Map<String, Object> item = new HashMap<>();
            item.put("value", bs.getBloodData() != null ? bs.getBloodData().doubleValue() : null);
            item.put("unit", "mmol/L");
            item.put("time", bs.getRecordTime() != null ? bs.getRecordTime().format(DATETIME_FORMATTER) : null);
            latest.add(item);
        }

        List<Object> dailyStats = new ArrayList<>();
        List<DailyAverageData> dailyData = dailyAverageDataMapper.selectByLatestTimes(userId, "blood_sugar", 30);
        for (DailyAverageData dad : dailyData) {
            Map<String, Object> item = new HashMap<>();
            item.put("date", dad.getRecordDate() != null ? dad.getRecordDate().format(DATE_FORMATTER) : null);
            item.put("avg", dad.getAverageValue() != null ? dad.getAverageValue().doubleValue() : null);
            dailyStats.add(item);
        }

        List<Object> weeklyStats = new ArrayList<>();
        List<WeeklyAverageData> weeklyData = weeklyAverageDataMapper.selectByLatestTimes(userId, "blood_sugar", 12);
        for (WeeklyAverageData wad : weeklyData) {
            Map<String, Object> item = new HashMap<>();
            item.put("week", wad.getWeekStartDate() != null ? wad.getWeekStartDate().format(DATE_FORMATTER) : null);
            item.put("avg", wad.getAverageValue() != null ? wad.getAverageValue().doubleValue() : null);
            weeklyStats.add(item);
        }

        List<Object> monthlyStats = new ArrayList<>();
        List<MonthlyAverageData> monthlyData = monthlyAverageDataMapper.selectByLatestTimes(userId, "blood_sugar", 6);
        for (MonthlyAverageData mad : monthlyData) {
            Map<String, Object> item = new HashMap<>();
            item.put("month", mad.getMonthDate() != null ? mad.getMonthDate().format(DATE_FORMATTER) : null);
            item.put("avg", mad.getAverageValue() != null ? mad.getAverageValue().doubleValue() : null);
            monthlyStats.add(item);
        }

        return new MonitoringData.BloodGlucoseData(latest, dailyStats, weeklyStats, monthlyStats);
    }

    private MonitoringData.PerfusionIndexData collectPerfusionIndexData(Long userId) {
        List<Object> latest = new ArrayList<>();
        List<PerfusionIndex> latestRecords = perfusionIndexMapper.getRecentData(userId, 5);
        for (PerfusionIndex pi : latestRecords) {
            Map<String, Object> item = new HashMap<>();
            item.put("value", pi.getPiData() != null ? pi.getPiData().doubleValue() : null);
            item.put("unit", "%");
            item.put("time", pi.getRecordTime() != null ? pi.getRecordTime().format(DATETIME_FORMATTER) : null);
            latest.add(item);
        }

        List<Object> dailyStats = new ArrayList<>();
        List<DailyAverageData> dailyData = dailyAverageDataMapper.selectByLatestTimes(userId, "perfusion_index", 30);
        for (DailyAverageData dad : dailyData) {
            Map<String, Object> item = new HashMap<>();
            item.put("date", dad.getRecordDate() != null ? dad.getRecordDate().format(DATE_FORMATTER) : null);
            item.put("avg", dad.getAverageValue() != null ? dad.getAverageValue().doubleValue() : null);
            dailyStats.add(item);
        }

        List<Object> weeklyStats = new ArrayList<>();
        List<WeeklyAverageData> weeklyData = weeklyAverageDataMapper.selectByLatestTimes(userId, "perfusion_index", 12);
        for (WeeklyAverageData wad : weeklyData) {
            Map<String, Object> item = new HashMap<>();
            item.put("week", wad.getWeekStartDate() != null ? wad.getWeekStartDate().format(DATE_FORMATTER) : null);
            item.put("avg", wad.getAverageValue() != null ? wad.getAverageValue().doubleValue() : null);
            weeklyStats.add(item);
        }

        List<Object> monthlyStats = new ArrayList<>();
        List<MonthlyAverageData> monthlyData = monthlyAverageDataMapper.selectByLatestTimes(userId, "perfusion_index", 6);
        for (MonthlyAverageData mad : monthlyData) {
            Map<String, Object> item = new HashMap<>();
            item.put("month", mad.getMonthDate() != null ? mad.getMonthDate().format(DATE_FORMATTER) : null);
            item.put("avg", mad.getAverageValue() != null ? mad.getAverageValue().doubleValue() : null);
            monthlyStats.add(item);
        }

        return new MonitoringData.PerfusionIndexData(latest, dailyStats, weeklyStats, monthlyStats);
    }

    private MonitoringData.BloodOxygenData collectBloodOxygenData(Long userId) {
        List<Object> latest = new ArrayList<>();
        List<BloodOxygen> latestRecords = bloodOxygenMapper.getRecentData(userId, 5);
        for (BloodOxygen bo : latestRecords) {
            Map<String, Object> item = new HashMap<>();
            item.put("value", bo.getOxygenData() != null ? bo.getOxygenData().intValue() : null);
            item.put("unit", "%");
            item.put("time", bo.getRecordTime() != null ? bo.getRecordTime().format(DATETIME_FORMATTER) : null);
            latest.add(item);
        }

        List<Object> dailyStats = new ArrayList<>();
        List<DailyAverageData> dailyData = dailyAverageDataMapper.selectByLatestTimes(userId, "blood_oxygen", 30);
        for (DailyAverageData dad : dailyData) {
            Map<String, Object> item = new HashMap<>();
            item.put("date", dad.getRecordDate() != null ? dad.getRecordDate().format(DATE_FORMATTER) : null);
            item.put("avg", dad.getAverageValue() != null ? dad.getAverageValue().intValue() : null);
            dailyStats.add(item);
        }

        List<Object> weeklyStats = new ArrayList<>();
        List<WeeklyAverageData> weeklyData = weeklyAverageDataMapper.selectByLatestTimes(userId, "blood_oxygen", 12);
        for (WeeklyAverageData wad : weeklyData) {
            Map<String, Object> item = new HashMap<>();
            item.put("week", wad.getWeekStartDate() != null ? wad.getWeekStartDate().format(DATE_FORMATTER) : null);
            item.put("avg", wad.getAverageValue() != null ? wad.getAverageValue().intValue() : null);
            weeklyStats.add(item);
        }

        List<Object> monthlyStats = new ArrayList<>();
        List<MonthlyAverageData> monthlyData = monthlyAverageDataMapper.selectByLatestTimes(userId, "blood_oxygen", 6);
        for (MonthlyAverageData mad : monthlyData) {
            Map<String, Object> item = new HashMap<>();
            item.put("month", mad.getMonthDate() != null ? mad.getMonthDate().format(DATE_FORMATTER) : null);
            item.put("avg", mad.getAverageValue() != null ? mad.getAverageValue().intValue() : null);
            monthlyStats.add(item);
        }

        return new MonitoringData.BloodOxygenData(latest, dailyStats, weeklyStats, monthlyStats);
    }

    private MonitoringData.SleepData collectSleepData(Long userId) {
        List<Object> latest = new ArrayList<>();
        List<SleepData> latestRecords = sleepDataMapper.getRecentData(userId, 5);
        for (SleepData sd : latestRecords) {
            Map<String, Object> item = new HashMap<>();
            item.put("value", sd.getSleepData() != null ? sd.getSleepData().doubleValue() : null);
            item.put("unit", "hours");
            item.put("time", sd.getRecordTime() != null ? sd.getRecordTime().format(DATETIME_FORMATTER) : null);
            latest.add(item);
        }

        List<Object> dailyStats = new ArrayList<>();
        List<DailyAverageData> dailyData = dailyAverageDataMapper.selectByLatestTimes(userId, "sleep", 30);
        for (DailyAverageData dad : dailyData) {
            Map<String, Object> item = new HashMap<>();
            item.put("date", dad.getRecordDate() != null ? dad.getRecordDate().format(DATE_FORMATTER) : null);
            item.put("avg", dad.getAverageValue() != null ? dad.getAverageValue().doubleValue() : null);
            dailyStats.add(item);
        }

        List<Object> weeklyStats = new ArrayList<>();
        List<WeeklyAverageData> weeklyData = weeklyAverageDataMapper.selectByLatestTimes(userId, "sleep", 12);
        for (WeeklyAverageData wad : weeklyData) {
            Map<String, Object> item = new HashMap<>();
            item.put("week", wad.getWeekStartDate() != null ? wad.getWeekStartDate().format(DATE_FORMATTER) : null);
            item.put("avg", wad.getAverageValue() != null ? wad.getAverageValue().doubleValue() : null);
            weeklyStats.add(item);
        }

        List<Object> monthlyStats = new ArrayList<>();
        List<MonthlyAverageData> monthlyData = monthlyAverageDataMapper.selectByLatestTimes(userId, "sleep", 6);
        for (MonthlyAverageData mad : monthlyData) {
            Map<String, Object> item = new HashMap<>();
            item.put("month", mad.getMonthDate() != null ? mad.getMonthDate().format(DATE_FORMATTER) : null);
            item.put("avg", mad.getAverageValue() != null ? mad.getAverageValue().doubleValue() : null);
            monthlyStats.add(item);
        }

        return new MonitoringData.SleepData(latest, dailyStats, weeklyStats, monthlyStats);
    }

    private MonitoringData.BloodPressureData collectBloodPressureData(Long userId) {
        List<Object> latest = new ArrayList<>();
        List<BloodPressure> latestRecords = bloodPressureMapper.getRecentData(userId, 5);
        for (BloodPressure bp : latestRecords) {
            Map<String, Object> item = new HashMap<>();
            item.put("systolic", bp.getSystolicBp() != null ? bp.getSystolicBp().intValue() : null);
            item.put("diastolic", bp.getDiastolicBp() != null ? bp.getDiastolicBp().intValue() : null);
            item.put("unit", "mmHg");
            item.put("time", bp.getRecordTime() != null ? bp.getRecordTime().format(DATETIME_FORMATTER) : null);
            latest.add(item);
        }

        List<Object> dailyStats = new ArrayList<>();
        List<BloodPressureDailyStats> dailyData = bloodPressureMapper.selectDailyStats(userId, 30);
        for (BloodPressureDailyStats ds : dailyData) {
            Map<String, Object> item = new HashMap<>();
            item.put("date", ds.getDate());
            item.put("avg_systolic", ds.getAvgSystolic() != null ? Math.round(ds.getAvgSystolic()) : null);
            item.put("avg_diastolic", ds.getAvgDiastolic() != null ? Math.round(ds.getAvgDiastolic()) : null);
            dailyStats.add(item);
        }

        List<Object> weeklyStats = new ArrayList<>();
        List<BloodPressureWeeklyStats> weeklyData = bloodPressureMapper.selectWeeklyStats(userId, 12);
        for (int i = 0; i < weeklyData.size(); i++) {
            BloodPressureWeeklyStats ws = weeklyData.get(i);
            Map<String, Object> item = new HashMap<>();
            item.put("week", ws.getWeek());
            item.put("avg_systolic", ws.getAvgSystolic() != null ? Math.round(ws.getAvgSystolic()) : null);
            item.put("avg_diastolic", ws.getAvgDiastolic() != null ? Math.round(ws.getAvgDiastolic()) : null);
            if (i < weeklyData.size() - 1) {
                BloodPressureWeeklyStats prev = weeklyData.get(i + 1);
                String trend = calculateTrend(ws.getAvgSystolic(), prev.getAvgSystolic());
                item.put("trend", trend);
            } else {
                item.put("trend", "stable");
            }
            weeklyStats.add(item);
        }

        List<Object> monthlyStats = new ArrayList<>();
        List<BloodPressureMonthlyStats> monthlyData = bloodPressureMapper.selectMonthlyStats(userId, 6);
        for (int i = 0; i < monthlyData.size(); i++) {
            BloodPressureMonthlyStats ms = monthlyData.get(i);
            Map<String, Object> item = new HashMap<>();
            item.put("month", ms.getMonth());
            item.put("avg_systolic", ms.getAvgSystolic() != null ? Math.round(ms.getAvgSystolic()) : null);
            item.put("avg_diastolic", ms.getAvgDiastolic() != null ? Math.round(ms.getAvgDiastolic()) : null);
            if (i < monthlyData.size() - 1) {
                BloodPressureMonthlyStats prev = monthlyData.get(i + 1);
                String trend = calculateTrend(ms.getAvgSystolic(), prev.getAvgSystolic());
                item.put("trend", trend);
            } else {
                item.put("trend", "stable");
            }
            monthlyStats.add(item);
        }

        return new MonitoringData.BloodPressureData(latest, dailyStats, weeklyStats, monthlyStats);
    }

    private String calculateTrend(Double current, Double previous) {
        if (current == null || previous == null || previous == 0) {
            return "stable";
        }
        double diff = current - previous;
        if (diff > 5) {
            return "up";
        } else if (diff < -5) {
            return "down";
        } else {
            return "stable";
        }
    }

    private UserProfile buildUserProfile(User user) {
        UserProfile profile = new UserProfile();
        profile.setUser_id(user.getId() != null ? user.getId().intValue() : null);
        profile.setGender(convertGender(user.getGender()));
        profile.setBirth_date(user.getBirthDate() != null ? user.getBirthDate().format(DATE_FORMATTER) : null);
        profile.setHeight(user.getHeight() != null ? user.getHeight().floatValue() : null);
        profile.setWeight(user.getWeight() != null ? user.getWeight().floatValue() : null);
        profile.setPast_medical_history(user.getPastMedicalHistory());
        profile.setFamily_history(user.getFamilyHistory());
        profile.setAllergy_history(user.getAllergyHistory());
        profile.setSurgical_history(user.getSurgicalHistory());
        profile.setMedical_compliance(user.getMedicalCompliance());
        return profile;
    }

    private String convertGender(String gender) {
        if (gender == null) {
            return "other";
        }
        switch (gender) {
            case "男":
            case "男性":
                return "male";
            case "女":
            case "女性":
                return "female";
            default:
                return "other";
        }
    }

    @Override
    public List<GetHealthReportListResponse> getHealthReportList(Long userId) {
        return healthReportMapper.getHealthReportList(userId);
    }

    @Override
    public HealthReport getHealthReport(Long healthReportId) {
        return healthReportMapper.getHealthReport(healthReportId);
    }

    @Override
    public void saveHealthReport(HealthReport healthReport) {
        healthReportMapper.insertHealthReport(healthReport.getUserId(), LocalDateTime.now(), healthReport.getReport());
    }

    @Override
    public void deleteHealthReport(Long healthReportId) {
        healthReportMapper.deleteHealthReport(healthReportId);
    }
}

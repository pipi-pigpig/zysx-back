package com.nurturing.Service.impI;

import com.nurturing.DTO.HeartDataByDateResponse;
import com.nurturing.DTO.MonthWeekData;
import com.nurturing.DTO.WeekDayData;
import com.nurturing.DTO.YearMonthData;
import com.nurturing.Mapper.DailyAverageDataMapper;
import com.nurturing.Mapper.HeartRateMapper;
import com.nurturing.Mapper.MonthlyAverageDataMapper;
import com.nurturing.Mapper.WeeklyAverageDataMapper;
import com.nurturing.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ZhangQinAn
 * @email 242646968@qq.com
 * @since 2025/12/14 下午6:50
 */
@Service
@RequiredArgsConstructor
public class HeartRateAggregatedService {

    private final HeartRateMapper heartRateMapper;
    private final DailyAverageDataMapper dailyMapper;
    private final WeeklyAverageDataMapper weeklyMapper;
    private final MonthlyAverageDataMapper monthlyMapper;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final String DATA_TYPE = "heart_rate"; // 心率数据类型

    /**
     * 1. 单日心率查询 - 基于 heart_rate_data 表
     */
    public List<HeartDataItem> getHeartDataByDate(Long userId, String dateStr) {
        LocalDate date = LocalDate.parse(dateStr, DATE_FORMATTER);

        // 查询原始表
        List<HeartRate> records = heartRateMapper.selectRawHeartRateByDate(userId, date);

        // 转换为响应 DTO（过滤 null 心率值）
        return records.stream()
                .filter(record -> record.getHeartData() != null) // 排除 heart_data IS NULL 的记录
                .map(record -> {
                    HeartDataItem item = new HeartDataItem();
                    item.setHeartData(record.getHeartData());
                    item.setRecordTime(record.getRecordTime());
                    return item;
                })
                .collect(Collectors.toList());
    }

    /**
     * 2. 查询周平均心率数据（使用 daily_average_data 表）
     */
    public List<WeekDayData> getHeartDataByWeek(Long userId, String dateInWeekStr) {
        LocalDate dateInWeek = LocalDate.parse(dateInWeekStr, DATE_FORMATTER);
        LocalDate monday = dateInWeek.with(DayOfWeek.MONDAY);
        LocalDate sunday = dateInWeek.with(DayOfWeek.SUNDAY);

        // 查询该周所有日平均数据
        List<DailyAverageData> records = dailyMapper.selectByWeek(
                userId, monday, sunday, DATA_TYPE
        );

        // 构建完整7天数据
        Map<LocalDate, BigDecimal> recordMap = records.stream()
                .collect(Collectors.toMap(DailyAverageData::getRecordDate, DailyAverageData::getAverageValue));

        List<WeekDayData> result = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            LocalDate currentDate = monday.plusDays(i);
            WeekDayData dayData = new WeekDayData();
            dayData.setDate(currentDate.format(DATE_FORMATTER));
            dayData.setAvgValue(recordMap.get(currentDate));
            result.add(dayData);
        }
        return result;
    }

    /**
     * 3. 查询月平均心率数据（使用 weekly_average_data 表）
     */
    public List<MonthWeekData> getHeartDataByMonth(Long userId, Integer year, Integer month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate monthStart = yearMonth.atDay(1).with(DayOfWeek.MONDAY);
        LocalDate monthEnd = yearMonth.atEndOfMonth().with(DayOfWeek.SUNDAY);

        // 直接查询周聚合表
        List<WeeklyAverageData> weeklyRecords = weeklyMapper.selectByWeekRange(
                userId, monthStart, monthEnd, DATA_TYPE
        );

        List<MonthWeekData> result = new ArrayList<>();
        for (WeeklyAverageData record : weeklyRecords) {
            LocalDate weekStart = record.getWeekStartDate();
            LocalDate weekEnd = weekStart.plusDays(6);

            // 确保周与查询月份有交集
            if (weekEnd.isBefore(yearMonth.atDay(1)) || weekStart.isAfter(yearMonth.atEndOfMonth())) {
                continue;
            }

            MonthWeekData weekData = new MonthWeekData();
            weekData.setWeekStart(weekStart.format(DATE_FORMATTER));
            weekData.setWeekEnd(weekEnd.format(DATE_FORMATTER));
            weekData.setAvgValue(record.getAverageValue());
            result.add(weekData);
        }
        return result;
    }

    /**
     * 4. 查询年平均心率数据（使用 monthly_average_data 表）
     */
    public List<YearMonthData> getHeartDataByYear(Long userId, Integer year) {
        List<MonthlyAverageData> monthlyRecords = monthlyMapper.selectByYear(
                userId, year, DATA_TYPE
        );

        Map<String, BigDecimal> monthMap = new HashMap<>();
        for (MonthlyAverageData record : monthlyRecords) {
            String monthKey = record.getMonthDate().format(DateTimeFormatter.ofPattern("yyyy-MM"));
            monthMap.put(monthKey, record.getAverageValue());
        }

        List<YearMonthData> result = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            String monthKey = String.format("%d-%02d", year, month);
            YearMonthData monthData = new YearMonthData();
            monthData.setMonth(monthKey);
            monthData.setAvgValue(monthMap.get(monthKey));
            result.add(monthData);
        }
        return result;
    }
}
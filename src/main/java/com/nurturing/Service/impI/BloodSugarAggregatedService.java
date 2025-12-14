package com.nurturing.Service.impI;

import com.nurturing.DTO.MonthWeekData;
import com.nurturing.DTO.YearMonthData;
import com.nurturing.Mapper.MonthlyAverageDataMapper;
import com.nurturing.Mapper.WeeklyAverageDataMapper;
import com.nurturing.entity.MonthlyAverageData;
import com.nurturing.entity.WeeklyAverageData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ZhangQinAn
 * @email 242646968@qq.com
 * @since 2025/12/10 下午5:48
 */
@Service
@RequiredArgsConstructor
public class BloodSugarAggregatedService {

    private final WeeklyAverageDataMapper weeklyMapper;
    private final MonthlyAverageDataMapper monthlyMapper;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final String DATA_TYPE = "blood_sugar";

    /**
     * 查询月平均血糖（使用 weekly_average_data 表）
     */
    public List<MonthWeekData> getBloodDataByMonth(Long userId, Integer year, Integer month) {
        // 1. 计算月份覆盖的周范围
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate monthStart = yearMonth.atDay(1).with(DayOfWeek.MONDAY);
        LocalDate monthEnd = yearMonth.atEndOfMonth().with(DayOfWeek.SUNDAY);

        // 2. 直接查询 weekly_average_data 表
        List<WeeklyAverageData> weeklyRecords = weeklyMapper.selectByWeekRange(
                userId, monthStart, monthEnd, DATA_TYPE
        );

        // 3. 转换为响应格式
        List<MonthWeekData> result = new ArrayList<>();
        for (WeeklyAverageData record : weeklyRecords) {
            // 确保周与查询月份有交集
            LocalDate weekStart = record.getWeekStartDate();
            LocalDate weekEnd = weekStart.plusDays(6);

            if (weekEnd.isBefore(yearMonth.atDay(1)) || weekStart.isAfter(yearMonth.atEndOfMonth())) {
                continue; // 跳过完全不在月份范围内的周
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
     * 查询年平均血糖（使用 monthly_average_data 表）
     */
    public List<YearMonthData> getBloodDataByYear(Long userId, Integer year) {
        // 1. 查询整年月数据
        List<MonthlyAverageData> monthlyRecords = monthlyMapper.selectByYear(userId, year,DATA_TYPE);

        // 2. 转换为 Map 便于查找
        java.util.Map<String, BigDecimal> monthMap = new java.util.HashMap<>();
        for (MonthlyAverageData record : monthlyRecords) {
            String monthKey = record.getMonthDate().format(DateTimeFormatter.ofPattern("yyyy-MM"));
            monthMap.put(monthKey, record.getAverageValue());
        }

        // 3. 生成全年12个月数据
        List<YearMonthData> result = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            String monthKey = String.format("%d-%02d", year, month);
            YearMonthData monthData = new YearMonthData();
            monthData.setMonth(monthKey);
            monthData.setAvgValue(monthMap.get(monthKey)); // 无数据时为null
            result.add(monthData);
        }
        return result;
    }
}
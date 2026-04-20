package com.nurturing.Service.impI;

import com.nurturing.DTO.SleepDataItem;
import com.nurturing.DTO.SleepMonthWeekData;
import com.nurturing.DTO.SleepWeekDayData;
import com.nurturing.DTO.SleepYearMonthData;
import com.nurturing.Mapper.DailyAverageDataMapper;
import com.nurturing.Mapper.MonthlyAverageDataMapper;
import com.nurturing.Mapper.SleepDataMapper;
import com.nurturing.Mapper.WeeklyAverageDataMapper;
import com.nurturing.entity.DailyAverageData;
import com.nurturing.entity.MonthlyAverageData;
import com.nurturing.entity.SleepData;
import com.nurturing.entity.WeeklyAverageData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SleepDataAggregatedService {

    private final SleepDataMapper sleepDataMapper;
    private final DailyAverageDataMapper dailyMapper;
    private final WeeklyAverageDataMapper weeklyMapper;
    private final MonthlyAverageDataMapper monthlyMapper;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final String DATA_TYPE = "sleep_data";

    public List<SleepDataItem> getSleepDataByDate(Long userId, String dateStr) {
        LocalDate date = LocalDate.parse(dateStr, DATE_FORMATTER);

        List<SleepData> records = sleepDataMapper.selectRawSleepByDate(userId, date);

        return records.stream()
                .filter(record -> record.getSleepData() != null)
                .map(record -> {
                    SleepDataItem item = new SleepDataItem();
                    item.setSleepDuration(record.getSleepData() != null ? record.getSleepData().intValue() : null);
                    item.setRecordTime(record.getRecordTime());
                    return item;
                })
                .collect(Collectors.toList());
    }

    public List<SleepWeekDayData> getSleepDataByWeek(Long userId, String dateInWeekStr) {
        LocalDate dateInWeek = LocalDate.parse(dateInWeekStr, DATE_FORMATTER);
        LocalDate monday = dateInWeek.with(DayOfWeek.MONDAY);
        LocalDate sunday = dateInWeek.with(DayOfWeek.SUNDAY);

        List<DailyAverageData> records = dailyMapper.selectByWeek(
                userId, monday, sunday, DATA_TYPE
        );

        Map<LocalDate, BigDecimal> recordMap = records.stream()
                .collect(Collectors.toMap(DailyAverageData::getRecordDate, DailyAverageData::getAverageValue));

        List<SleepWeekDayData> result = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            LocalDate currentDate = monday.plusDays(i);
            SleepWeekDayData dayData = new SleepWeekDayData();
            dayData.setDate(currentDate.format(DATE_FORMATTER));
            BigDecimal avgValue = recordMap.get(currentDate);
            dayData.setAvgSleepDuration(avgValue != null ? avgValue.setScale(0, RoundingMode.HALF_UP).intValue() : null);
            result.add(dayData);
        }
        return result;
    }

    public List<SleepMonthWeekData> getSleepDataByMonth(Long userId, Integer year, Integer month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate monthStart = yearMonth.atDay(1).with(DayOfWeek.MONDAY);
        LocalDate monthEnd = yearMonth.atEndOfMonth().with(DayOfWeek.SUNDAY);

        List<WeeklyAverageData> weeklyRecords = weeklyMapper.selectByWeekRange(
                userId, monthStart, monthEnd, DATA_TYPE
        );

        List<SleepMonthWeekData> result = new ArrayList<>();
        int weekNumber = 1;
        for (WeeklyAverageData record : weeklyRecords) {
            LocalDate weekStart = record.getWeekStartDate();
            LocalDate weekEnd = weekStart.plusDays(6);

            if (weekEnd.isBefore(yearMonth.atDay(1)) || weekStart.isAfter(yearMonth.atEndOfMonth())) {
                continue;
            }

            SleepMonthWeekData weekData = new SleepMonthWeekData();
            weekData.setWeek(weekNumber++);
            BigDecimal avgValue = record.getAverageValue();
            weekData.setAvgSleepDuration(avgValue != null ? avgValue.setScale(0, RoundingMode.HALF_UP).intValue() : null);
            result.add(weekData);
        }
        return result;
    }

    public List<SleepYearMonthData> getSleepDataByYear(Long userId, Integer year) {
        List<MonthlyAverageData> monthlyRecords = monthlyMapper.selectByYear(
                userId, year, DATA_TYPE
        );

        Map<Integer, BigDecimal> monthMap = new HashMap<>();
        for (MonthlyAverageData record : monthlyRecords) {
            int monthValue = record.getMonthDate().getMonthValue();
            monthMap.put(monthValue, record.getAverageValue());
        }

        List<SleepYearMonthData> result = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            SleepYearMonthData monthData = new SleepYearMonthData();
            monthData.setMonth(month);
            BigDecimal avgValue = monthMap.get(month);
            monthData.setAvgSleepDuration(avgValue != null ? avgValue.setScale(0, RoundingMode.HALF_UP).intValue() : null);
            result.add(monthData);
        }
        return result;
    }
}

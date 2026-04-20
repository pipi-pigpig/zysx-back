package com.nurturing.Service.impI;

import com.nurturing.DTO.OxygenDataItem;
import com.nurturing.DTO.OxygenMonthWeekData;
import com.nurturing.DTO.OxygenWeekDayData;
import com.nurturing.DTO.OxygenYearMonthData;
import com.nurturing.Mapper.BloodOxygenMapper;
import com.nurturing.Mapper.DailyAverageDataMapper;
import com.nurturing.Mapper.MonthlyAverageDataMapper;
import com.nurturing.Mapper.WeeklyAverageDataMapper;
import com.nurturing.entity.BloodOxygen;
import com.nurturing.entity.DailyAverageData;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BloodOxygenAggregatedService {

    private final BloodOxygenMapper bloodOxygenMapper;
    private final DailyAverageDataMapper dailyMapper;
    private final WeeklyAverageDataMapper weeklyMapper;
    private final MonthlyAverageDataMapper monthlyMapper;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final String DATA_TYPE = "blood_oxygen";

    public List<OxygenDataItem> getOxygenDataByDate(Long userId, String dateStr) {
        LocalDate date = LocalDate.parse(dateStr, DATE_FORMATTER);

        List<BloodOxygen> records = bloodOxygenMapper.selectRawOxygenByDate(userId, date);

        return records.stream()
                .filter(record -> record.getOxygenData() != null)
                .map(record -> {
                    OxygenDataItem item = new OxygenDataItem();
                    item.setOxygenData(record.getOxygenData());
                    item.setRecordTime(record.getRecordTime());
                    return item;
                })
                .collect(Collectors.toList());
    }

    public List<OxygenWeekDayData> getOxygenDataByWeek(Long userId, String dateInWeekStr) {
        LocalDate dateInWeek = LocalDate.parse(dateInWeekStr, DATE_FORMATTER);
        LocalDate monday = dateInWeek.with(DayOfWeek.MONDAY);
        LocalDate sunday = dateInWeek.with(DayOfWeek.SUNDAY);

        List<DailyAverageData> records = dailyMapper.selectByWeek(
                userId, monday, sunday, DATA_TYPE
        );

        Map<LocalDate, BigDecimal> recordMap = records.stream()
                .collect(Collectors.toMap(DailyAverageData::getRecordDate, DailyAverageData::getAverageValue));

        List<OxygenWeekDayData> result = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            LocalDate currentDate = monday.plusDays(i);
            OxygenWeekDayData dayData = new OxygenWeekDayData();
            dayData.setDate(currentDate.format(DATE_FORMATTER));
            dayData.setAvgOxygen(recordMap.get(currentDate));
            result.add(dayData);
        }
        return result;
    }

    public List<OxygenMonthWeekData> getOxygenDataByMonth(Long userId, Integer year, Integer month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate monthStart = yearMonth.atDay(1).with(DayOfWeek.MONDAY);
        LocalDate monthEnd = yearMonth.atEndOfMonth().with(DayOfWeek.SUNDAY);

        List<WeeklyAverageData> weeklyRecords = weeklyMapper.selectByWeekRange(
                userId, monthStart, monthEnd, DATA_TYPE
        );

        List<OxygenMonthWeekData> result = new ArrayList<>();
        int weekNumber = 1;
        for (WeeklyAverageData record : weeklyRecords) {
            LocalDate weekStart = record.getWeekStartDate();
            LocalDate weekEnd = weekStart.plusDays(6);

            if (weekEnd.isBefore(yearMonth.atDay(1)) || weekStart.isAfter(yearMonth.atEndOfMonth())) {
                continue;
            }

            OxygenMonthWeekData weekData = new OxygenMonthWeekData();
            weekData.setWeek(weekNumber++);
            weekData.setAvgOxygen(record.getAverageValue());
            result.add(weekData);
        }
        return result;
    }

    public List<OxygenYearMonthData> getOxygenDataByYear(Long userId, Integer year) {
        List<MonthlyAverageData> monthlyRecords = monthlyMapper.selectByYear(
                userId, year, DATA_TYPE
        );

        Map<Integer, BigDecimal> monthMap = new HashMap<>();
        for (MonthlyAverageData record : monthlyRecords) {
            int monthValue = record.getMonthDate().getMonthValue();
            monthMap.put(monthValue, record.getAverageValue());
        }

        List<OxygenYearMonthData> result = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            OxygenYearMonthData monthData = new OxygenYearMonthData();
            monthData.setMonth(month);
            monthData.setAvgOxygen(monthMap.get(month));
            result.add(monthData);
        }
        return result;
    }
}

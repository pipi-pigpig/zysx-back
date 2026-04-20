package com.nurturing.Service.impI;

import com.nurturing.DTO.*;
import com.nurturing.Mapper.DailyAverageDataMapper;
import com.nurturing.Mapper.MonthlyAverageDataMapper;
import com.nurturing.Mapper.PerfusionIndexMapper;
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

@Service
@RequiredArgsConstructor
public class PerfusionIndexAggregatedService {

    private final PerfusionIndexMapper perfusionIndexMapper;
    private final DailyAverageDataMapper dailyMapper;
    private final WeeklyAverageDataMapper weeklyMapper;
    private final MonthlyAverageDataMapper monthlyMapper;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final String DATA_TYPE = "perfusion_index";

    public List<PiDataItem> getPiDataByDate(Long userId, String dateStr) {
        LocalDate date = LocalDate.parse(dateStr, DATE_FORMATTER);

        List<PerfusionIndex> records = perfusionIndexMapper.selectRawPiByDate(userId, date);

        return records.stream()
                .filter(record -> record.getPiData() != null)
                .map(record -> {
                    PiDataItem item = new PiDataItem();
                    item.setPiData(record.getPiData());
                    item.setRecordTime(record.getRecordTime());
                    return item;
                })
                .collect(Collectors.toList());
    }

    public List<PiWeekDayData> getPiDataByWeek(Long userId, String dateInWeekStr) {
        LocalDate dateInWeek = LocalDate.parse(dateInWeekStr, DATE_FORMATTER);
        LocalDate monday = dateInWeek.with(DayOfWeek.MONDAY);
        LocalDate sunday = dateInWeek.with(DayOfWeek.SUNDAY);

        List<DailyAverageData> records = dailyMapper.selectByWeek(
                userId, monday, sunday, DATA_TYPE
        );

        Map<LocalDate, BigDecimal> recordMap = records.stream()
                .collect(Collectors.toMap(DailyAverageData::getRecordDate, DailyAverageData::getAverageValue));

        List<PiWeekDayData> result = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            LocalDate currentDate = monday.plusDays(i);
            PiWeekDayData dayData = new PiWeekDayData();
            dayData.setDate(currentDate.format(DATE_FORMATTER));
            dayData.setAvgPi(recordMap.get(currentDate));
            result.add(dayData);
        }
        return result;
    }

    public List<PiMonthWeekData> getPiDataByMonth(Long userId, Integer year, Integer month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate monthStart = yearMonth.atDay(1).with(DayOfWeek.MONDAY);
        LocalDate monthEnd = yearMonth.atEndOfMonth().with(DayOfWeek.SUNDAY);

        List<WeeklyAverageData> weeklyRecords = weeklyMapper.selectByWeekRange(
                userId, monthStart, monthEnd, DATA_TYPE
        );

        List<PiMonthWeekData> result = new ArrayList<>();
        int weekNumber = 1;
        for (WeeklyAverageData record : weeklyRecords) {
            LocalDate weekStart = record.getWeekStartDate();
            LocalDate weekEnd = weekStart.plusDays(6);

            if (weekEnd.isBefore(yearMonth.atDay(1)) || weekStart.isAfter(yearMonth.atEndOfMonth())) {
                continue;
            }

            PiMonthWeekData weekData = new PiMonthWeekData();
            weekData.setWeek(weekNumber++);
            weekData.setAvgPi(record.getAverageValue());
            result.add(weekData);
        }
        return result;
    }

    public List<PiYearMonthData> getPiDataByYear(Long userId, Integer year) {
        List<MonthlyAverageData> monthlyRecords = monthlyMapper.selectByYear(
                userId, year, DATA_TYPE
        );

        Map<Integer, BigDecimal> monthMap = new HashMap<>();
        for (MonthlyAverageData record : monthlyRecords) {
            int monthValue = record.getMonthDate().getMonthValue();
            monthMap.put(monthValue, record.getAverageValue());
        }

        List<PiYearMonthData> result = new ArrayList<>();
        for (int month = 1; month <= 12; month++) {
            PiYearMonthData monthData = new PiYearMonthData();
            monthData.setMonth(month);
            monthData.setAvgPi(monthMap.get(month));
            result.add(monthData);
        }
        return result;
    }
}

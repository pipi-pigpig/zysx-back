package com.nurturing.Service.impI;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nurturing.DTO.BloodPressureDailyRecord;
import com.nurturing.DTO.BloodPressureMonthlyAverage;
import com.nurturing.DTO.BloodPressureWeeklyAverage;
import com.nurturing.DTO.BloodPressureYearlyAverage;
import com.nurturing.Event.BloodOxygenDataEvent;
import com.nurturing.Event.BloodPressureDataEvent;
import com.nurturing.Mapper.BloodPressureMapper;
import com.nurturing.Service.BloodPressureService;
import com.nurturing.entity.BloodOxygen;
import com.nurturing.entity.BloodPressure;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class BloodPressureServiceImpl extends ServiceImpl<BloodPressureMapper,BloodPressure> implements BloodPressureService {


    @Autowired
    private BloodPressureMapper bloodPressureMapper;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Override
    public List<BloodPressure> getById(Long userId) {
        return bloodPressureMapper.getById(userId);
    }

    @Override
    public List<BloodPressure> getRecentData(Long userId) {
        return bloodPressureMapper.getRecentData(userId, 10);
    }

    @Override
    @Transactional
    public boolean save(BloodPressure entity) {
        boolean result = super.save(entity);
        if (result) {
            // 发布血压数据事件，触发WebSocket推送
            eventPublisher.publishEvent(new BloodPressureDataEvent(this, entity));
        }
        return result;
    }
    @Override
    @Transactional
    public void saveHealthData(BloodPressure entity) {
        save(entity);
    }


    @Override
    @Transactional
    public void saveFromMQTT(BloodPressure bloodPressure, String mac) {
        bloodPressureMapper.insertByMac(bloodPressure.getSystolicBp(),bloodPressure.getDiastolicBp(),mac, LocalDateTime.now());
    }


    // 1. 单日血压查询
    @Override
    public List<BloodPressureDailyRecord> getDailyRecords(Long userId, String dateStr) {
        LocalDate date = LocalDate.parse(dateStr);
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = start.plusDays(1);
        return bloodPressureMapper.selectDailyRecords(userId, start, end);
    }

    // 2. 周平均血压查询
    public List<BloodPressureWeeklyAverage> getWeeklyAverages(Long userId, String dateInWeekStr) {
        LocalDate dateInWeek = LocalDate.parse(dateInWeekStr);
        LocalDate monday = dateInWeek.minusDays(dateInWeek.getDayOfWeek().getValue() - 1);
        LocalDate sunday = monday.plusDays(6);

        return bloodPressureMapper.selectWeeklyAverages(
                userId,
                monday.atStartOfDay(),
                sunday.atTime(23, 59, 59)
        );
    }

    // 3. 月平均血压查询
    public List<BloodPressureMonthlyAverage> getMonthlyAverages(Long userId, int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate firstDay = yearMonth.atDay(1);
        LocalDate lastDay = yearMonth.atEndOfMonth();

        List<BloodPressureDailyRecord> rawData = bloodPressureMapper.selectMonthlyRawData(
                userId,
                firstDay.atStartOfDay(),
                lastDay.atTime(23, 59, 59)
        );

        List<WeeklyRange> weeks = generateWeeksInMonth(firstDay, lastDay);
        Map<LocalDate, List<BloodPressureDailyRecord>> groupedByDay = rawData.stream()
                .collect(Collectors.groupingBy(r -> r.getRecordTime().toLocalDate()));

        List<BloodPressureMonthlyAverage> result = new ArrayList<>();
        for (WeeklyRange week : weeks) {
            List<BloodPressureDailyRecord> weekRecords = new ArrayList<>();
            LocalDate day = week.start;
            while (!day.isAfter(week.end)) {
                if (!day.isBefore(firstDay) && !day.isAfter(lastDay)) {
                    weekRecords.addAll(groupedByDay.getOrDefault(day, Collections.emptyList()));
                }
                day = day.plusDays(1);
            }

            BloodPressureMonthlyAverage avg = new BloodPressureMonthlyAverage();
            avg.setWeekStart(week.start.toString());
            avg.setWeekEnd(week.end.toString());

            if (!weekRecords.isEmpty()) {
                double avgSys = weekRecords.stream()
                        .mapToInt(BloodPressureDailyRecord::getSystolicBp)
                        .average()
                        .orElse(0);
                double avgDia = weekRecords.stream()
                        .mapToInt(BloodPressureDailyRecord::getDiastolicBp)
                        .average()
                        .orElse(0);
                avg.setAvgSystolic(avgSys);
                avg.setAvgDiastolic(avgDia);
            } else {
                avg.setAvgSystolic(0.0);
                avg.setAvgDiastolic(0.0);
            }
            result.add(avg);
        }
        return result;
    }

    // 4. 年平均血压查询
    public List<BloodPressureYearlyAverage> getYearlyAverages(Long userId, int year) {
        return bloodPressureMapper.selectYearlyAverages(userId, year);
    }

    // 内部工具类
    private static class WeeklyRange {
        final LocalDate start;
        final LocalDate end;

        WeeklyRange(LocalDate start, LocalDate end) {
            this.start = start;
            this.end = end;
        }
    }

    private List<WeeklyRange> generateWeeksInMonth(LocalDate firstDay, LocalDate lastDay) {
        LocalDate startMonday = firstDay.minusDays(firstDay.getDayOfWeek().getValue() - 1);
        LocalDate endSunday = lastDay.plusDays(7 - lastDay.getDayOfWeek().getValue());

        List<WeeklyRange> weeks = new ArrayList<>();
        LocalDate currentMonday = startMonday;
        while (!currentMonday.isAfter(endSunday)) {
            weeks.add(new WeeklyRange(currentMonday, currentMonday.plusDays(6)));
            currentMonday = currentMonday.plusDays(7);
        }
        return weeks;
    }
}

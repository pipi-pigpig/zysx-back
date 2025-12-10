package com.nurturing.Service.impI;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.nurturing.DTO.WeekDayData;
import com.nurturing.Event.BloodOxygenDataEvent;
import com.nurturing.Event.BloodSugarDataEvent;
import com.nurturing.Mapper.BloodSugarMapper;
import com.nurturing.Mapper.DailyAverageDataMapper;
import com.nurturing.Service.BloodSugarService;
import com.nurturing.entity.BloodOxygen;
import com.nurturing.entity.BloodSugar;
import com.nurturing.entity.DailyAverageData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BloodSugarServiceImpl extends ServiceImpl<BloodSugarMapper,BloodSugar> implements BloodSugarService {

    @Autowired
    private BloodSugarMapper bloodSugarMapper;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private DailyAverageDataMapper dailyAverageDataMapper;

    @Override
    public List<BloodSugar> getById(Long userId) {

        return bloodSugarMapper.getById(userId);
    }

    @Override
    public List<BloodSugar> getRecentData(Long userId) {
        return bloodSugarMapper.getRecentData(userId, 10);
    }

    @Override
    @Transactional
    public boolean save(BloodSugar entity) {
        boolean result = super.save(entity);
        if (result) {
            // 发布血糖数据事件，触发WebSocket推送
            eventPublisher.publishEvent(new BloodSugarDataEvent(this, entity));
        }
        return result;
    }
    @Override
    @Transactional
    public void saveHealthData(BloodSugar entity) {
        save(entity);
    }

    @Override
    @Transactional
    public void saveFromMQTT(BloodSugar bloodData, String mac) {
        bloodSugarMapper.insertByMac(bloodData.getBloodData(),mac, LocalDateTime.now());
    }


    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public List<WeekDayData> getBloodDataByWeek(Long userId, String dateInWeekStr) {
        // 1. 计算周一和周日
        LocalDate dateInWeek = LocalDate.parse(dateInWeekStr, DATE_FORMATTER);
        LocalDate monday = dateInWeek.with(DayOfWeek.MONDAY);
        LocalDate sunday = dateInWeek.with(DayOfWeek.SUNDAY);

        // 2. 查询数据库
        List<DailyAverageData> records = dailyAverageDataMapper.selectBloodSugarByWeek(userId, monday, sunday);

        // 3. 构建完整7天数据（缺失日期补null）
        Map<LocalDate, BigDecimal> recordMap = records.stream()
                .collect(Collectors.toMap(DailyAverageData::getRecordDate, DailyAverageData::getAverageValue));

        List<WeekDayData> result = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            LocalDate currentDate = monday.plusDays(i);
            WeekDayData dayData = new WeekDayData();
            dayData.setDate(currentDate.format(DATE_FORMATTER));
            dayData.setAvgValue(recordMap.get(currentDate)); // 无数据时为null
            result.add(dayData);
        }
        return result;
    }

}

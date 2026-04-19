package com.nurturing.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nurturing.DTO.BloodPressureDailyRecord;
import com.nurturing.DTO.BloodPressureDailyStats;
import com.nurturing.DTO.BloodPressureMonthlyStats;
import com.nurturing.DTO.BloodPressureWeeklyAverage;
import com.nurturing.DTO.BloodPressureWeeklyStats;
import com.nurturing.DTO.BloodPressureYearlyAverage;
import com.nurturing.entity.BloodOxygen;
import com.nurturing.entity.BloodPressure;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface BloodPressureMapper extends BaseMapper<BloodPressure> {

        @Select("select * from blood_pressure_data where user_id=#{userId}")
        List<BloodPressure> getById(Long userId);

        @Select("select * from blood_pressure_data where user_id=#{userId} order by record_time desc limit #{limit}")
        List<BloodPressure> getRecentData(@Param("userId") Long userId, @Param("limit") Integer limit);

        @Insert("insert into blood_pressure_data(user_id, systolic_bp, diastolic_bp, record_time) select user_id ,#{systolicBp},#{diastolicBp},#{recordTime} from equipment where equipment.mac=#{mac}")
        void insertByMac(@Param("systolicBp") BigDecimal systolicBp, @Param("diastolicBp") BigDecimal diastolicBp,
                        @Param("mac") String mac, @Param("recordTime") LocalDateTime recordTime);

        List<BloodPressureDailyRecord> selectDailyRecords(
                        @Param("userId") Long userId,
                        @Param("startDate") LocalDateTime startDate,
                        @Param("endDate") LocalDateTime endDate);

        List<BloodPressureWeeklyAverage> selectWeeklyAverages(
                        @Param("userId") Long userId,
                        @Param("weekStart") LocalDateTime weekStart,
                        @Param("weekEnd") LocalDateTime weekEnd);

        // 原始数据查询直接使用BloodPressureDailyRecord
        List<BloodPressureDailyRecord> selectMonthlyRawData(
                        @Param("userId") Long userId,
                        @Param("monthStart") LocalDateTime monthStart,
                        @Param("monthEnd") LocalDateTime monthEnd);

        List<BloodPressureYearlyAverage> selectYearlyAverages(
                        @Param("userId") Long userId,
                        @Param("year") int year);

        List<BloodPressureDailyStats> selectDailyStats(
                        @Param("userId") Long userId,
                        @Param("limit") Integer limit);

        List<BloodPressureWeeklyStats> selectWeeklyStats(
                        @Param("userId") Long userId,
                        @Param("limit") Integer limit);

        List<BloodPressureMonthlyStats> selectMonthlyStats(
                        @Param("userId") Long userId,
                        @Param("limit") Integer limit);
}

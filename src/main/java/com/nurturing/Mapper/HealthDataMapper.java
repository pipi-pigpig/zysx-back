package com.nurturing.Mapper;

import com.nurturing.entity.HealthData;
import org.apache.ibatis.annotations.*;

@Mapper
public interface HealthDataMapper {
    // 血氧数据操作
    @Insert("INSERT INTO bloodoxygendata (user_id, created_at, oxygenData) " +
            "VALUES (#{userId}, #{recordTime}, #{spo2})")
    void insertBloodOxygen(HealthData data);

    @Delete("DELETE FROM bloodoxygendata WHERE user_id = #{userId} AND created_at = " +
            "(SELECT created_at FROM (SELECT created_at FROM bloodoxygendata WHERE user_id = #{userId} " +
            "ORDER BY created_at ASC LIMIT 1) AS tmp)")
    void deleteOldestBloodOxygen(Integer userId);

    @Select("SELECT COUNT(*) FROM bloodoxygendata WHERE user_id = #{userId}")
    int countBloodOxygenByUser(Integer userId);

    // 心率数据操作
    @Insert("INSERT INTO heartratedata (user_id, created_at, heartData) " +
            "VALUES (#{userId}, #{recordTime}, #{bmp})")
    void insertHeartRate(HealthData data);

    @Delete("DELETE FROM heartratedata WHERE user_id = #{userId} AND created_at = " +
            "(SELECT created_at FROM (SELECT created_at FROM heartratedata WHERE user_id = #{userId} " +
            "ORDER BY created_at ASC LIMIT 1) AS tmp)")
    void deleteOldestHeartRate(Integer userId);

    @Select("SELECT COUNT(*) FROM heartratedata WHERE user_id = #{userId}")
    int countHeartRateByUser(Integer userId);

    // PI 数据操作
    @Insert("INSERT INTO perfusionindexdata (user_id, created_at, piData) " +
            "VALUES (#{userId}, #{recordTime}, #{pi})")
    void insertPerfusionIndex(HealthData data);

    @Delete("DELETE FROM perfusionindexdata WHERE user_id = #{userId} AND created_at = " +
            "(SELECT created_at FROM (SELECT created_at FROM perfusionindexdata WHERE user_id = #{userId} " +
            "ORDER BY created_at ASC LIMIT 1) AS tmp)")
    void deleteOldestPerfusionIndex(Integer userId);

    @Select("SELECT COUNT(*) FROM perfusionindexdata WHERE user_id = #{userId}")
    int countPerfusionIndexByUser(Integer userId);
}
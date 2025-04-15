package com.nurturing.Mapper;

import com.nurturing.entity.HealthData;
import org.apache.ibatis.annotations.*;

@Mapper
public interface HealthDataMapper {
    @Insert("REPLACE INTO health_data(id, spo2, bmp, pi, device_code, mac, record_time) " +
            "VALUES(#{id}, #{spo2}, #{bmp}, #{pi}, #{deviceCode}, #{mac}, #{recordTime})")
    void replace(HealthData data);
}
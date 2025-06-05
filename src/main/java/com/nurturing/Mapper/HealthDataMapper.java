package com.nurturing.Mapper;

import com.nurturing.entity.HealthData;
import com.nurturing.entity.HeartData;
import com.nurturing.entity.OxygenData;
import com.nurturing.entity.PiData;
import org.apache.ibatis.annotations.*;

@Mapper
public interface HealthDataMapper {
    @Insert("REPLACE INTO health_data(id, spo2, bmp, pi, device_code, mac, record_time,user_id) " +
            "VALUES(#{id}, #{spo2}, #{bmp}, #{pi}, #{deviceCode}, #{mac}, #{recordTime},#{user_id})")
    void replace(HealthData data);

    @Insert("REPLACE INTO bloodoxygendata(BloodOxygenID, user_id, created_at, oxygenData) VALUES " +
            "(#{BloodOxygenID},#{user_id},#{created_at},#{oxygenData})")
    void replaceOxygen(OxygenData oxygenData);

    @Insert("REPLACE INTO heartratedata(HeartRateID, user_id, created_at, heartData) VALUES " +
            "(#{HeartRateID},#{user_id},#{created_at},#{heartData})")
    void replaceHeart(HeartData heartData);

    @Insert("REPLACE INTO perfusionindexdata(PerfusionIndexID, user_id, created_at, piData) VALUES " +
            "(#{PerfusionIndexID},#{user_id},#{created_at},#{piData})")
    void replacePi(PiData piData);
}
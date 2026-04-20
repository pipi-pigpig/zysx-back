package com.nurturing.Mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nurturing.entity.BloodOxygen;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface BloodOxygenMapper extends BaseMapper<BloodOxygen> {

    @Select("select  * from blood_oxygen_data where user_id=#{userId} order by record_time  ")
    List<BloodOxygen> getById(Long userId);

    @Select("select * from blood_oxygen_data where user_id=#{userId} order by record_time desc limit #{limit}")
    List<BloodOxygen> getRecentData(@Param("userId") Long userId, @Param("limit") Integer limit);

    @Insert("insert into blood_oxygen_data(user_id, oxygen_data, record_time) select user_id,#{oxygenData}, #{recordTime} from equipment where equipment.mac=#{mac}")
    void insertByMac(@Param("oxygenData") BigDecimal oxygenData, @Param("mac") String mac, @Param("recordTime")LocalDateTime recordTime);

    List<BloodOxygen> selectRawOxygenByDate(@Param("userId") Long userId, @Param("date") LocalDate date);
}

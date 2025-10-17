package com.nurturing.Mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nurturing.entity.BloodOxygen;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface BloodOxygenMapper extends BaseMapper<BloodOxygen> {

    @Select("select  * from bloodoxygendata where user_id=#{userId} order by created_at  ")
    List<BloodOxygen> getById(Long userId);

    @Insert("insert into blood_oxygen_data(user_id, oxygen_data, record_time) select user_id,#{oxygenData}, #{recordTime} from equipment where equipment.mac=#{mac}")
    void insertByMac(@Param("oxygenData") BigDecimal oxygenData, @Param("mac") String mac, @Param("recordTime")LocalDateTime recordTime);

}

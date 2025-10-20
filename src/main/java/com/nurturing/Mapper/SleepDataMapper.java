package com.nurturing.Mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nurturing.entity.SleepData;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface SleepDataMapper extends BaseMapper<SleepData> {

    @Select("select * from sleep_data where user_id=#{userId}")
    List<SleepData> getById(Long userId);

    @Insert("insert into sleep_data(user_id, sleep_data, record_time) select user_id,#{sleepData},#{recordTime} from equipment where equipment.mac=#{mac}")
    void insertByMac(@Param("sleepData") BigDecimal sleepData, @Param("mac")String mac, @Param("recordTime")LocalDateTime recordTime);
}

package com.nurturing.Mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nurturing.entity.HeartRate;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface HeartRateMapper extends BaseMapper<HeartRate> {
    @Select("select  * from heartratedata where user_id=#{userId} order by created_at")
    List<HeartRate> getById(Long userId);

    @Insert("insert into heart_rate_data(user_id, heart_data, record_time) select user_id,#{heartData},#{recordTime} from equipment where equipment.mac=#{mac}")
    void insertByMac(@Param("heartData") BigDecimal heartData, @Param("mac") String mac, @Param("recordTime")LocalDateTime recordTime);
}

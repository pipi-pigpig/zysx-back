package com.nurturing.Mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nurturing.DTO.HeartDataByDateResponse;
import com.nurturing.entity.BloodOxygen;
import com.nurturing.entity.HeartRate;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface HeartRateMapper extends BaseMapper<HeartRate> {
    @Select("select  * from heart_rate_data where user_id=#{userId} order by record_time")
    List<HeartRate> getById(Long userId);

    @Select("select * from heart_rate_data where user_id=#{userId} order by record_time desc limit #{limit}")
    List<HeartRate> getRecentData(@Param("userId") Long userId, @Param("limit") Integer limit);

    @Insert("insert into heart_rate_data(user_id, heart_data, record_time) select user_id,#{heartData},#{recordTime} from equipment where equipment.mac=#{mac}")
    void insertByMac(@Param("heartData") BigDecimal heartData, @Param("mac") String mac, @Param("recordTime")LocalDateTime recordTime);

    /**
     * 查询单日原始心率记录
     */

    List<HeartRate> selectRawHeartRateByDate(
            @Param("userId") Long userId,
            @Param("date") LocalDate date
    );
}

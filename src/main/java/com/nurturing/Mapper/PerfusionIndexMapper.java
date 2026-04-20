package com.nurturing.Mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nurturing.entity.BloodOxygen;
import com.nurturing.entity.PerfusionIndex;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface PerfusionIndexMapper extends BaseMapper<PerfusionIndex> {

    List<PerfusionIndex> selectRawPiByDate(@Param("userId") Long userId, @Param("date") LocalDate date);
    @Select("select  * from perfusion_index_data where user_id=#{userId}  order by record_time")
    List<PerfusionIndex> getById(Long userId);

    @Insert("insert into perfusion_index_data(user_id, pi_data, record_time) select user_id ,#{piData},#{recordTime} from equipment where equipment.mac=#{mac}")
    void insertByMac(@Param("piData") BigDecimal piData, @Param("mac") String mac, @Param("recordTime")LocalDateTime recordTime);

    @Select("select * from perfusion_index_data where user_id=#{userId} order by record_time desc limit #{limit}")
    List<PerfusionIndex> getRecentData(@Param("userId") Long userId, @Param("limit") Integer limit);
}

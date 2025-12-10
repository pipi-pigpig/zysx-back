package com.nurturing.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nurturing.entity.BloodOxygen;
import com.nurturing.entity.BloodSugar;
import com.nurturing.entity.BloodSugarRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface BloodSugarMapper extends BaseMapper<BloodSugar> {


    @Select("select  * from blood_sugar_data where user_id=#{userId}")
    List<BloodSugar> getById(Long userId);

    @Select("select * from blood_sugar_data where user_id=#{userId} order by record_time desc limit #{limit}")
    List<BloodSugar> getRecentData(@Param("userId") Long userId, @Param("limit") Integer limit);

    @Insert("insert into blood_sugar_data(user_id, blood_data, record_time) select user_Id,#{bloodData},#{recordTime} from equipment where equipment.mac=#{mac}")
    void insertByMac(@Param("bloodData") BigDecimal bloodData,@Param("mac") String mac,@Param("recordTime") LocalDateTime recordTime);

    /**
     * 根据用户ID和日期查询原始血糖记录
     * 严格匹配表结构：使用 blood_sugar_value 和 record_time 字段
     */
    List<BloodSugarRecord> selectByUserAndDate(
            @Param("userId") Long userId,
            @Param("date") LocalDate date
    );
}

package com.nurturing.Mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nurturing.entity.BloodSugar;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface BloodSugarMapper extends BaseMapper<BloodSugar> {


    @Select("select  * from blood_sugar_data where user_id=#{userId}")
    List<BloodSugar> getById(Long userId);

    @Insert("insert into blood_sugar_data(user_id, blood_data, record_time) select user_Id,#{bloodData},#{recordTime} from equipment where equipment.mac=#{mac}")
    void insertByMac(@Param("bloodData") BigDecimal bloodData,@Param("mac") String mac,@Param("recordTime") LocalDateTime recordTime);
}

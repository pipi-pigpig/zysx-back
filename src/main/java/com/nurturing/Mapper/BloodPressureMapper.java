package com.nurturing.Mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.nurturing.entity.BloodPressure;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface BloodPressureMapper extends BaseMapper<BloodPressure> {

    @Select("select * from bloodpressuredata where user_id=#{userId}")
    List<BloodPressure> getById(Long userId);

    @Insert("insert into blood_pressure_data(user_id, systolic_bp, diastolic_bp, record_time) select user_id ,#{systolicBp},#{diastolicBp},#{recordTime} from equipment where equipment.mac=#{mac}")
    void insertByMac(@Param("systolicBp") BigDecimal systolicBp,@Param("diastolicBp") BigDecimal diastolicBp, @Param("mac") String mac, @Param("recordTime") LocalDateTime recordTime);
}

package com.nurturing.Mapper;

import com.nurturing.entity.BloodData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BloodDataMapper {


    @Select("select  * from bloodsugardata where user_id=#{userId}")
    List<BloodData> getById(Long userId);
}

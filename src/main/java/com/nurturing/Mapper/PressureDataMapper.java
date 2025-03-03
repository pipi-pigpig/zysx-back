package com.nurturing.Mapper;


import com.nurturing.entity.PressureData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PressureDataMapper {

    @Select("select * from bloodpressuredata where user_id=#{userId}")
    List<PressureData> getById(Long userId);
}

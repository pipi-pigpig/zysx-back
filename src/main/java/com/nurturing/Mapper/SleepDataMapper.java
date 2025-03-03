package com.nurturing.Mapper;


import com.nurturing.entity.SleepData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SleepDataMapper {

    @Select("select * from sleepdata where user_id=#{userId}")
    List<SleepData> getById(Long userId);

}

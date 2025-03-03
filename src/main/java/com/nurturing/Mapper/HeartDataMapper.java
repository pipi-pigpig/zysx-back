package com.nurturing.Mapper;


import com.nurturing.entity.HeartData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface HeartDataMapper {
    @Select("select  * from heartratedata where user_id=#{userId}")
    List<HeartData> getById(Long userId);
}

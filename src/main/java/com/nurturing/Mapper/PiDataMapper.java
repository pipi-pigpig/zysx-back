package com.nurturing.Mapper;


import com.nurturing.entity.PiData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PiDataMapper {
    @Select("select  * from perfusionindexdata where user_id=#{userId}  order by created_at")
    List<PiData> getById(Long userId);
}

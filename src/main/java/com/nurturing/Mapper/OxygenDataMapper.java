package com.nurturing.Mapper;


import com.nurturing.entity.OxygenData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface OxygenDataMapper {

    @Select("select  * from bloodoxygendata where user_id=#{userId} order by created_at  ")
    List<OxygenData> getById(Long userId);



}

package com.nurturing.Mapper;


import com.nurturing.entity.PersonalHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PersonalHistoryMapper {


    @Select("select * from user_medical_history where user_id=#{userId}")
    PersonalHistory getById(Long userId);
}

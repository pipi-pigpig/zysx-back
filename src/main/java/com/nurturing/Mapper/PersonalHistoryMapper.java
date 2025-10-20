package com.nurturing.Mapper;


import com.nurturing.entity.PersonalHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PersonalHistoryMapper {


    @Select("select id,family_history,allergy_history,past_medical_history,surgical_history,medical_compliance from users where id=#{userId}")
    PersonalHistory getById(Long  userId);

}

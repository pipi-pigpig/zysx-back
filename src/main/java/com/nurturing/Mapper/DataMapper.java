package com.nurturing.Mapper;


import com.nurturing.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface DataMapper {
    @Select("select * from users where  user_id=#{userId}")
    User getById(Long userId);

}

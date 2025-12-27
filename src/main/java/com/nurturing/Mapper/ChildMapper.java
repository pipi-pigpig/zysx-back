package com.nurturing.Mapper;


import com.nurturing.entity.Child;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ChildMapper {
    Child selectByAccount(@Param("account") String account);
    Child selectById(@Param("id") Long id);
}
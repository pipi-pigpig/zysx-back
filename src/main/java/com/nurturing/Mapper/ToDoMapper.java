package com.nurturing.Mapper;


import com.nurturing.entity.ToDo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ToDoMapper {

    @Select("select * from todo_calendar where start_date=#{date} and user_id=#{userId}")
    List<ToDo> getToDos(LocalDate date, long userId);
}

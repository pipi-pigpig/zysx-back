package com.nurturing.Mapper;


import com.nurturing.entity.ToDo;
import com.nurturing.entity.ToDoVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ToDoMapper {

    List<ToDoVo> getToDos(@Param("date") LocalDate date, @Param("userId") Long userId);

    @Insert("insert into todo_calendar ( user_id, todo_type, event_name, start_date, start_time, end_date, completed) " +
            "values (#{userId},#{todoType},#{eventName},#{startDate},#{startTime},#{endDate},#{completed}) ")
    ToDo insert(ToDo toDo);
}

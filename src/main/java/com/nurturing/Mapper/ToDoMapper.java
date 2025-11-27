package com.nurturing.Mapper;


import com.nurturing.entity.ToDo;
import com.nurturing.entity.ToDoVo;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Mapper
public interface ToDoMapper {

    List<ToDoVo> getToDos(@Param("date") LocalDate date, @Param("userId") Long userId);


    // 插入主表，返回自增 id
    @Insert({
            "<script>",
            "INSERT INTO todo_calendar (user_id, todo_type, event_name, start_date, start_time, end_date, completed)",
            "VALUES (#{userId}, #{todoType}, #{eventName}, #{startDate}, #{startTime}, #{endDate}, #{completed})",
            "</script>"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    int insertTodoCalendar(ToDo record); // 返回 int（影响行数），id 会自动填入 record.getId()

    // 插入用药子表
    @Insert("INSERT INTO schedule_medication (todo_id, dosage) VALUES (#{todoId}, #{dosage})")
    void insertMedication(@Param("todoId") Long todoId, @Param("dosage") BigDecimal dosage);

    // 插入日程子表
    @Insert("INSERT INTO schedule_daily (todo_id, end_time, location, remarks) " +
            "VALUES (#{todoId}, #{endTime}, #{location}, #{remarks})")
    void insertDailySchedule(
            @Param("todoId") Long todoId,
            @Param("endTime") LocalTime endTime,
            @Param("location") String location,
            @Param("remarks") String remarks
    );


    // 更新主表（只更新非 null 字段）
    @Update({
            "<script>",
            "UPDATE todo_calendar",
            "SET ",
            "  <if test='todoType != null'>todo_type = #{todoType},</if>",
            "  <if test='eventName != null'>event_name = #{eventName},</if>",
            "  <if test='startDate != null'>start_date = #{startDate},</if>",
            "  <if test='startTime != null'>start_time = #{startTime},</if>",
            "  <if test='endDate != null'>end_date = #{endDate},</if>",
            "  <if test='completed != null'>completed = #{completed}</if>",
            "WHERE id = #{id}",
            "</script>"
    })
    int updateTodoCalendar(
            @Param("id") Long id,
            @Param("todoType") String todoType,
            @Param("eventName") String eventName,
            @Param("startDate") LocalDate startDate,
            @Param("startTime") LocalTime startTime,
            @Param("endDate") LocalDate endDate,
            @Param("completed") Integer completed
    );

    // 删除子表记录
    @Delete("DELETE FROM schedule_medication WHERE todo_id = #{todoId}")
    void deleteMedicationByTodoId(@Param("todoId") Long todoId);

    @Delete("DELETE FROM schedule_daily WHERE todo_id = #{todoId}")
    void deleteDailyScheduleByTodoId(@Param("todoId") Long todoId);


    @Delete("DELETE FROM todo_calendar WHERE id = #{id}")
    int deleteTodoCalendarById(@Param("id") Long id);

    @Update("UPDATE todo_calendar SET completed = #{completed} WHERE id = #{id}")
    int updateTodoCompleted(@Param("id") Long id, @Param("completed") Integer completed);
}

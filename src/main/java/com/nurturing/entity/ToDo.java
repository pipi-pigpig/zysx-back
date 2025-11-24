package com.nurturing.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.time.LocalDateTime;

@lombok.Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("todo_calendar")
public class ToDo {

    private  long id;
    private long user_id;
    private String todo_type;
    private String event_name;
    private LocalDateTime start_date;
    private Time start_time;
    private LocalDateTime end_date;
    private Integer completed;


}

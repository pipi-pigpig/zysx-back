package com.nurturing.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;

@lombok.Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("todo_calendar")
public class ToDo {

    private  long id;
    private long userId;
    private String todoType;
    private String eventName;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Time startTime;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    private Integer completed;


}

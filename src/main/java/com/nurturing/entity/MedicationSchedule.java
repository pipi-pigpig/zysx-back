package com.nurturing.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.sql.Time;

@lombok.Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("schedule_medication")
public class MedicationSchedule {
    private long todo_id;
    private Time end_time;
    private  String location;
    private TextArea remarks;

}

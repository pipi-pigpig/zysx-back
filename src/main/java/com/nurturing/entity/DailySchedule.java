package com.nurturing.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@lombok.Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("schedule_daily")
public class DailySchedule {
    private long todo_id;
    private BigDecimal dosage;

}

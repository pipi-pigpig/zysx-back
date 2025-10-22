package com.nurturing.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.nurturing.Service.HealthData;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@lombok.Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("heart_rate_data")
public class HeartRate implements HealthData {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private BigDecimal heartData; // 心率值
    private Date recordTime;

    @Override
    public String getDataType() {
        return "heartRate";
    }

    // getter 和 setter 方法
    @Override
    public Long getUserId() { return userId; }
    @Override
    public Date getRecordTime() { return recordTime; }

    // ... 其他getter/setter

    public HeartRate(BigDecimal heartData) {
        this.heartData = heartData;
    }
}

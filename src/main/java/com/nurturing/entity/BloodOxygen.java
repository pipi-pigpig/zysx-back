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
@TableName("blood_oxygen_data")
public class BloodOxygen implements HealthData {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private BigDecimal oxygenData; // 血氧值
    private LocalDateTime recordTime;

    @Override
    public String getDataType() {
        return "bloodOxygen";
    }

    // getter 和 setter 方法
    @Override
    public Long getUserId() { return userId; }
    @Override
    public LocalDateTime getRecordTime() { return recordTime; }

    public BloodOxygen(BigDecimal oxygenData) {
        this.oxygenData = oxygenData;
    }

    // ... 其他getter/setter
}

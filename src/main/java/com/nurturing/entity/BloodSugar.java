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

@lombok.Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("blood_sugar_data")
public class BloodSugar implements HealthData {

    @TableId(type = IdType.AUTO)
    private long id;
    private long userId;
    private LocalDateTime recordTime;
    private BigDecimal bloodData;


    @Override
    public String getDataType() {
        return "bloodSugar";
    }

    // getter 和 setter 方法
    @Override
    public Long getUserId() { return userId; }
    @Override
    public LocalDateTime getRecordTime() { return recordTime; }

    public BloodSugar(BigDecimal bloodData) {
        this.bloodData = bloodData;
    }

}

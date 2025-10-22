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
// PerfusionIndex 实体类
@TableName("perfusion_index_data")
public class PerfusionIndex implements HealthData {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private BigDecimal piData; // 灌注指数
    private Date recordTime;

    @Override
    public String getDataType() {
        return "perfusionIndex";
    }

    // getter 和 setter 方法
    @Override
    public Long getUserId() { return userId; }
    @Override
    public Date getRecordTime() { return recordTime; }

    public PerfusionIndex(BigDecimal piData) {
        this.piData = piData;
    }
    // ... 其他getter/setter
}

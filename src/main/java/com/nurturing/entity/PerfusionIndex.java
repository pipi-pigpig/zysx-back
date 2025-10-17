package com.nurturing.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@lombok.Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("perfusion_index_data")
public class PerfusionIndex {

    @TableId(type = IdType.AUTO)
    private long id;

    private long userId;

    @JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss")
    private LocalDateTime recordTime;

    private BigDecimal piData;

    public PerfusionIndex(BigDecimal piData) {
        this.piData = piData;
    }
}

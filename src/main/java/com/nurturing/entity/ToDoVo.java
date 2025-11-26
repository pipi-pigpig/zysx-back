package com.nurturing.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.math.BigDecimal;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * @author ZhangQinAn
 * @email 242646968@qq.com
 * @since 2025/11/26 上午11:33
 */

@lombok.Data
@NoArgsConstructor
@AllArgsConstructor
public class ToDoVo {

    private Long id;
    private String eventName;
    private String todoType;       // "medication" 或 "schedule"
    private BigDecimal dosage;
    // 仅用药有值
    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime startTime;

    @JsonFormat(pattern = "HH:mm:ss")
    private LocalTime endTime;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    private String location;       // 仅日程有值
    private String remarks;        // 仅日程有值
    private Integer completed;
}

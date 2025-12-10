package com.nurturing.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author ZhangQinAn
 * @email 242646968@qq.com
 * @since 2025/12/10 上午10:37
 */
@Data
@TableName("blood_sugar_data")
public class BloodSugarRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private BigDecimal bloodSugarValue;  // 对应 blood_sugar_value 字段
    private LocalDateTime recordTime;    // 对应 record_time 字段
}
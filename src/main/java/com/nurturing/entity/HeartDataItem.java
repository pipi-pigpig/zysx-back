package com.nurturing.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @author ZhangQinAn
 * @email 242646968@qq.com
 * @since 2025/12/14 下午9:13
 */
@Data
public class HeartDataItem {
    private BigDecimal heartData;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime recordTime;
}

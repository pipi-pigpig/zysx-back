package com.nurturing.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author ZhangQinAn
 * @email 242646968@qq.com
 * @since 2025/12/18 上午9:02
 */
@Data
public class BloodPressureDailyRecord {
    private Integer systolicBp;
    private Integer diastolicBp;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime recordTime;
}

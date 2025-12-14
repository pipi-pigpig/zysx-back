package com.nurturing.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author ZhangQinAn
 * @email 242646968@qq.com
 * @since 2025/12/14 下午8:23
 */
@Data
public class HeartDataByDateResponse {
    private Integer heartData;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime recordTime;
}

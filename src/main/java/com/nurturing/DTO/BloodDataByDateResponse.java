package com.nurturing.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author ZhangQinAn
 * @email 242646968@qq.com
 * @since 2025/12/10 上午10:42
 */
@Data
public class BloodDataByDateResponse {
    private Integer code = 200;
    private String message = "success";
    private List<BloodRecordItem> data;

    @Data
    public static class BloodRecordItem {
        private BigDecimal bloodData;  // 对应接口规范中的bloodData

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime recordTime;  // 严格匹配接口要求的格式
    }
}
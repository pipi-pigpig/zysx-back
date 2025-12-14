package com.nurturing.DTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author ZhangQinAn
 * @email 242646968@qq.com
 * @since 2025/12/14 下午6:47
 */
@Data
public class HeartDataByMonthRequest {
    @NotNull(message = "userId不能为空")
    private Long userId;

    @NotNull(message = "年份不能为空")
    @Min(value = 1900, message = "年份无效")
    private Integer year;

    @NotNull(message = "月份不能为空")
    @Min(value = 1, message = "月份应在1-12之间")
    @Max(value = 12, message = "月份应在1-12之间")
    private Integer month;
}


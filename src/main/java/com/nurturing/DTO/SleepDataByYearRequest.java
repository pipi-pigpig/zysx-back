package com.nurturing.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SleepDataByYearRequest {
    @NotNull(message = "userId不能为空")
    private Long userId;

    @NotNull(message = "年份不能为空")
    @Min(value = 1900, message = "年份无效")
    private Integer year;
}

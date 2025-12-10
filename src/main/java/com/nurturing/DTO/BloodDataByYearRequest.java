package com.nurturing.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @author ZhangQinAn
 * @email 242646968@qq.com
 * @since 2025/12/10 下午5:46
 */
@Data
public class BloodDataByYearRequest {
    @NotNull
    private Long userId;
    @NotNull @Min(1900) private Integer year;
}
package com.nurturing.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * @author ZhangQinAn
 * @email 242646968@qq.com
 * @since 2025/12/10 上午11:43
 */
@Data
public class BloodDataByWeekRequest {
    @NotNull(message = "userId不能为空")
    private Long userId;

    @NotBlank(message = "dateInWeek不能为空")
    @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])$",
            message = "日期格式必须为YYYY-MM-DD")
    private String dateInWeek;
}

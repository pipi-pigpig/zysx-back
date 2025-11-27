package com.nurturing.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author ZhangQinAn
 * @email 242646968@qq.com
 * @since 2025/11/27 上午9:53
 */
@Data
public class UpdateTodoStatusRequest {
    @JsonProperty("completed")
    private Integer completed; // 0 或 1
}
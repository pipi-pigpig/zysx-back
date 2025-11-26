package com.nurturing.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author ZhangQinAn
 * @email 242646968@qq.com
 * @since 2025/11/26 下午12:49
 */
@Data
public class QueryTodoRequest {
    @JsonProperty("start_date")
    private String startDate; // 对应 JSON 中的 "start_date"

    @JsonProperty("user_id")
    private Long userId;      // 对应 "user_id"
}

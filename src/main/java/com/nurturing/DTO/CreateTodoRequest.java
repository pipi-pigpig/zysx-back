package com.nurturing.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author ZhangQinAn
 * @email 242646968@qq.com
 * @since 2025/11/27 上午8:20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTodoRequest {
    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("eventName")
    private String eventName;

    @JsonProperty("todoType")
    private String todoType; // "medication" 或 "schedule"

    @JsonProperty("startTime")
    private String startTime; // "08:00:00"

    @JsonProperty("endTime")
    private String endTime; // 仅 schedule 用，但 medication 也可传（可忽略）

    @JsonProperty("startDate")
    private String startDate; // "2025-11-24"

    @JsonProperty("endDate")
    private String endDate; // "2025-12-24"

    @JsonProperty("dosage")
    private BigDecimal dosage; // 仅 medication

    @JsonProperty("location")
    private String location; // 仅 schedule

    @JsonProperty("remarks")
    private String remarks; // 仅 schedule

    @JsonProperty("completed")
    private Integer completed; // 0 or 1
}

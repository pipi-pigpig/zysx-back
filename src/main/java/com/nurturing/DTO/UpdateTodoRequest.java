package com.nurturing.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * @author ZhangQinAn
 * @email 242646968@qq.com
 * @since 2025/11/27 上午9:09
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTodoRequest {
    @JsonProperty("eventName")
    private String eventName;

    @JsonProperty("todoType")
    private String todoType; // "medication" 或 "schedule"

    @JsonProperty("startTime")
    private String startTime;

    @JsonProperty("endTime")
    private String endTime;

    @JsonProperty("startDate")
    private String startDate;

    @JsonProperty("endDate")
    private String endDate;

    @JsonProperty("dosage")
    private BigDecimal dosage;

    @JsonProperty("location")
    private String location;

    @JsonProperty("remarks")
    private String remarks;

    @JsonProperty("completed")
    private Integer completed;
}

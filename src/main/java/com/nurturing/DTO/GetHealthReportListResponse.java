package com.nurturing.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GetHealthReportListResponse {
    Long id;
    LocalDateTime createTime;
}

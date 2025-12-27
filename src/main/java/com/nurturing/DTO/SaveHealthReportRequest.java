package com.nurturing.DTO;

import lombok.Data;

@Data
public class SaveHealthReportRequest {
    Long userId;
    String report;
}

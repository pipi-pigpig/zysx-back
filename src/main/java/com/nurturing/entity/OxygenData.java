package com.nurturing.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@lombok.Data
@NoArgsConstructor
@AllArgsConstructor
public class OxygenData {
    private long BloodOxygenID;
    private long user_id;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime created_at;
    private String oxygenData;
}

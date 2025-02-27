package com.nurturing.entity;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@lombok.Data
@NoArgsConstructor
@AllArgsConstructor
public class BloodData {

    private long BloodSugarID;
    private long user_id;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime Date;
    private String bloodData;


}

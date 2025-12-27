package com.nurturing.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class AddParentRequest {
    private String username;
    @JsonProperty("birth_date")
    private String birthDate;
    private String gender; // M/F
    private BigDecimal height;
    private BigDecimal weight;
    private String phone;
    private String avatar;
}
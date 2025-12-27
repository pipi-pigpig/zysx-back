package com.nurturing.vo;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ParentInfoVO {
    private Long id;
    private String username;
    private LocalDate birthDate;
    private String gender;
    private BigDecimal height;
    private BigDecimal weight;
    private String phone;
    private String avatar;
    private Long lastActive; // 保留但可选
}
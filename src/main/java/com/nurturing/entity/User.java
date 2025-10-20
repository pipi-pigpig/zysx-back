package com.nurturing.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private Long id;
    private String username;
    private String account;
    private String password;
    private String phone;
    private String avatar;
    private String gender;
    private LocalDate birthDate;
    private BigDecimal height;
    private BigDecimal weight;
    private String pastMedicalHistory;
    private String familyHistory;
    private String allergyHistory;
    private String surgicalHistory;
    private String medicalCompliance;
}


package com.nurturing.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.DecimalFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPageCenterDataDTO {
    private int id;
    private String username;
    private String gender;
    private String phone;
    private int weight;
    private int height;
    private String family_history;
    private String allergy_history;
    private String past_medical_history;
    private String surgical_history;
    private String medical_compliance;

}

package com.nurturing.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.DecimalFormat;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPageCenterDataDTO {
    private int user_id;
    private String Username;
    private String gender;
    private int Age;
    private String phone_number;
    private int Weight;
    private int Height;
    private String family_history;
    private String allergy_history;
    private String past_medical_history;
    private String surgical_history;
    private String medical_compliance;

}

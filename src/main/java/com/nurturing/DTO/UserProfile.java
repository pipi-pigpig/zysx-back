package com.nurturing.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {
    private Integer user_id;
    private String gender;
    private String birth_date;
    private Float height;
    private Float weight;
    private String past_medical_history;
    private String family_history;
    private String allergy_history;
    private String surgical_history;
    private String medical_compliance;
}

package com.nurturing.entity;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@lombok.Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonalHistory {

    private long user_id;
    private String family_history;
    private String allergy_history;
    private String past_medical_history;
    private String surgical_history;
    private String medication_compliance;

}

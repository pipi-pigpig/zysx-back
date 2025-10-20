package com.nurturing.entity;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.jdom2.Text;

@lombok.Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonalHistory {

    private long id;
    private String familyHistory;
    private String allergyHistory;
    private String pastMedicalHistory;
    private String surgicalHistory;
    private String medicalCompliance;

}

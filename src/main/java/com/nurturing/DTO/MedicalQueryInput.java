package com.nurturing.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicalQueryInput {
    private String text;

    // getter和setter
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
}

package com.nurturing.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryResponse {
    private String answer;

    // getter和setter
    public String getAnswer() { return answer; }
    public void setAnswer(String answer) { this.answer = answer; }
}

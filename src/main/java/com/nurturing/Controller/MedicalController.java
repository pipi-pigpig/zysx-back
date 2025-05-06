package com.nurturing.Controller;

import com.nurturing.Service.impI.MedicalQAService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;

@RestController
//@RequestMapping("/api/medical")
public class MedicalController {
    @Autowired
    private MedicalQAService medicalQAService;

    @PostMapping("/query")
    public ResponseEntity<?> askQuestion(@RequestBody Map<String, String> request) {
        try {
            System.out.println("question: " + request.get("question"));
            String answer = medicalQAService.queryMedicalQA(request.get("question"));
            return ResponseEntity.ok().body(Collections.singletonMap("answer", answer));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}

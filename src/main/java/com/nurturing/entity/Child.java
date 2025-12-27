package com.nurturing.entity;

import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class Child {
    private Long id;
    private String account;
    private String username;
    private String password;
    private String phone;
    private String avatar;
    private String email;
    private String gender; // male, female, other
    private LocalDate birthDate;
    private String relationship;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
package com.nurturing.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {


    private long user_id;
    private String username;
    private String password;
    private String account;
    private String avatar;
    private String age;
    private String gender;
    private String phone_number;
    private String height;
    private String weight;


}

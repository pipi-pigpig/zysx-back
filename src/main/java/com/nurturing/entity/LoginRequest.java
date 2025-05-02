package com.nurturing.entity;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@lombok.Data
@NoArgsConstructor
@AllArgsConstructor
// 定义一个 LoginRequest 类
public class LoginRequest {
    private String account;
    private String password;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
    // getters and setters

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

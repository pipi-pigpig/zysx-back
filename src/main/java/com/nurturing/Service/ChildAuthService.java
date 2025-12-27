package com.nurturing.Service;

import com.nurturing.vo.LoginRequest;
import com.nurturing.vo.LoginResponse;

public interface ChildAuthService {
    LoginResponse login(LoginRequest request);
}
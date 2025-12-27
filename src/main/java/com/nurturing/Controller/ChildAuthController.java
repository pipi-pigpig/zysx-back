package com.nurturing.Controller;


import com.nurturing.Service.ChildAuthService;
import com.nurturing.vo.LoginRequest;
import com.nurturing.vo.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/child")
@CrossOrigin
public class ChildAuthController {

    @Autowired
    private ChildAuthService childAuthService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {

        LoginResponse response = childAuthService.login(request);
        return ResponseEntity.ok(response);
    }
}
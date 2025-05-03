package com.nurturing.Controller;


import com.nurturing.DTO.UserPageCenterDataDTO;
import com.nurturing.Service.UserLoginService;
//import com.nurturing.entity.Address;
import com.nurturing.entity.LoginRequest;
import com.nurturing.entity.User;
import com.nurturing.result.Result;
import com.nurturing.utils.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@Slf4j
@RestController
//@RequestMapping("/userInfo")
public class UserLoginController {

    @Autowired
    private UserLoginService userLoginService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

//    @PostMapping("/userInfo1")
//    public Result<User> login(@RequestBody String username,@RequestBody String password) {
//        System.out.println(username+":"+password);
//        User user = userLoginService.getUserInfo(username, password);
//        if (user != null) {
//            return Result.success(user);
//        } else {
//            return Result.error("用户不存在或密码错误");
//        }
//
//    }
@PostMapping("/userInfo")
public Result<User> login(@RequestBody LoginRequest request) {
    String account = request.getAccount();
    String password = request.getPassword();

    System.out.println(account+":"+password);
        User user = userLoginService.getUserInfo(account, password);
        if (user != null) {
            user.setToken(jwtTokenUtil.generateToken(user.getUsername()));
            return Result.success(user);
        } else {
            return Result.error("用户不存在或密码错误");
        }

}

    @PostMapping
    public  Result updateUser(@RequestBody long user_id,@RequestBody String username) {


        userLoginService.updateUser(user_id,username);
        return Result.success();
    }

    /*
    fetchUserpagecenterdata
     * 保存用户的基本信息
     * 请求参数：
     * user_id:string
     * Username:string
     * gender:string
     * Age:int
     * phone_number:string
     * Height:Int
     * Weight:Int
     * family_history:string
     * allergy_history:string
     * past_medical_history:string
     * surgical_history:string
     * medical_compliance:string
     * 响应参数：
     * 返回成功或者失败
     */
    @PostMapping("/fetchUserPageCenterData")
    public String fetchUserPageCenterData(@RequestBody UserPageCenterDataDTO userPageCenterDataDTO) {


        try {
            log.info("更新用户信息:{}", userPageCenterDataDTO);
            userLoginService.fetchUserPageCenterData(userPageCenterDataDTO);
            return "更新用户信息成功";
        } catch (Exception e) {
            return "更新用户信息失败";
        }

    }
}

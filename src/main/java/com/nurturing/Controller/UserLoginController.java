package com.nurturing.Controller;


import com.nurturing.Service.UserLoginService;
//import com.nurturing.entity.Address;
import com.nurturing.entity.LoginRequest;
import com.nurturing.entity.User;
import com.nurturing.result.Result;
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

    @PostMapping("/userInfo1")
    public Result<User> login(@RequestBody String username,@RequestBody String password) {
        System.out.println(username+":"+password);
        User user = userLoginService.getUserInfo(username, password);
        if (user != null) {
            return Result.success(user);
        } else {
            return Result.error("用户不存在或密码错误");
        }

    }
@PostMapping("/userInfo")
public Result<User> login(@RequestBody LoginRequest request) {
    String username = request.getUsername();
    String password = request.getPassword();

    System.out.println(username+":"+password);
        User user = userLoginService.getUserInfo(username, password);
        if (user != null) {
            return Result.success(user);
        } else {
            return Result.error("用户不存在或密码错误");
        }

}




//    @GetMapping("/{user_id}")
//    public List<Address> getAddress(@PathVariable long user_id) {
//
//        List<Address> addresses=userLoginService.getById(user_id);
//        return addresses;
//    }

    @PostMapping
    public  Result updateUser(@RequestBody long user_id,@RequestBody String username) {


        userLoginService.updateUser(user_id,username);
        return Result.success();
    }

//    @DeleteMapping("/{addr_id}")
//    public Result deleteAttraction(@PathVariable long addr_id) {
//
//        userLoginService.deleteAddr(addr_id);
//        return Result.success();
//    }

}

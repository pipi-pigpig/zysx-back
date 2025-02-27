package com.nurturing.Service.impI;


import com.nurturing.Mapper.UserLoginMapper;
import com.nurturing.Service.UserLoginService;
//import com.nurturing.entity.Address;
import com.nurturing.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class UserLoginServiceImpI implements UserLoginService {
    @Autowired
    private UserLoginMapper userLoginMapper;

    @Override
    public User getUserInfo(String username, String password) {
        User user = userLoginMapper.findByUsername(username);

        if (user != null && user.getPassword().equals(password)) { // 这里可以放入加密校验逻辑
            return user;
        }
        return null; // 用户不存在或密码错误
    }

//    @Override
//    public List<Address> getById(long userId) {
//
//        List<Address> addresses=userLoginMapper.getById(userId);
//        return addresses;
//    }

    @Override
    public void updateUser(long userId, String username) {
        userLoginMapper.updateUser(userId,username);

    }

//    @Override
//    public void deleteAddr(long addrId) {
//
//        userLoginMapper.deleteAddr(addrId);
//    }
}


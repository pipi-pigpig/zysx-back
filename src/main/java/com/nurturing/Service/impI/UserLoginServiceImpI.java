package com.nurturing.Service.impI;


import com.nurturing.DTO.UserPageCenterDataDTO;
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
    public User getUserInfo(String account, String password) {
        User user = userLoginMapper.findByUsername(account);

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

    @Override
    public void fetchUserPageCenterData(UserPageCenterDataDTO userPageCenterDataDTO) {

        int user_id = userPageCenterDataDTO.getUser_id();
        String username = userPageCenterDataDTO.getUsername();
        String gender = userPageCenterDataDTO.getGender();
        int age = userPageCenterDataDTO.getAge();
        String phone_number=userPageCenterDataDTO.getPhone_number();
        int weight = userPageCenterDataDTO.getWeight();
        int height = userPageCenterDataDTO.getHeight();
        String family_history=userPageCenterDataDTO.getFamily_history();
        String allergy_history=userPageCenterDataDTO.getAllergy_history();
        String past_medical_history=userPageCenterDataDTO.getPast_medical_history();
        String surgical_history=userPageCenterDataDTO.getSurgical_history();
        String medical_compliance=userPageCenterDataDTO.getMedical_compliance();

        userLoginMapper.updateUsers(user_id,username,age,gender,phone_number,height,weight);

        userLoginMapper.updateUserHistory(user_id,family_history,allergy_history,past_medical_history,surgical_history,medical_compliance);
    }

//    @Override
//    public void deleteAddr(long addrId) {
//
//        userLoginMapper.deleteAddr(addrId);
//    }
}


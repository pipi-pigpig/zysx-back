package com.nurturing.Service;

//import com.nurturing.entity.Address;
import com.nurturing.DTO.UserPageCenterDataDTO;
import com.nurturing.entity.User;

import java.util.List;

public interface UserLoginService {
    User getUserInfo(String account, String password);

//    List<Address> getById(long userId);

    void updateUser(long userId, String username);

    void fetchUserPageCenterData(UserPageCenterDataDTO userPageCenterDataDTO);

//    void deleteAddr(long addrId);
}

package com.nurturing.Service;

//import com.nurturing.entity.Address;
import com.nurturing.entity.User;

import java.util.List;

public interface UserLoginService {
    User getUserInfo(String username, String password);

//    List<Address> getById(long userId);

    void updateUser(long userId, String username);

//    void deleteAddr(long addrId);
}

package com.nurturing.Mapper;


//import com.nurturing.entity.Address;
import com.nurturing.entity.User;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface UserLoginMapper {

    @Select("SELECT * FROM users WHERE username = #{username}")
    User findByUsername(String username);


//    @Select("select user_addr.address,addr_id from user_addr where user_id=#{userId}")
//    List<Address> getById(long userId);

    @Update("update users set username=#{username} where user_id=#{userId} ")
    void updateUser(long userId, String username);


//    @Delete("delete  from user_addr where addr_id=#{addrId}")
//    void deleteAddr(long addrId);
}

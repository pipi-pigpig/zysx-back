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

    @Select("SELECT * FROM users WHERE account = #{account}")
    User findByUsername(String account);


//    @Select("select user_addr.address,addr_id from user_addr where user_id=#{userId}")
//    List<Address> getById(long userId);

    @Update("update users set username=#{username} where user_id=#{userId} ")
    void updateUser(long userId, String username);

    @Update("update  users set Username=#{username},Age=#{age},gender=#{gender},phone_number=#{phoneNumber},Height=#{height},Weight=#{weight} where user_id=#{userId}")
    void updateUsers(int userId, String username, int age, String gender, String phoneNumber, int height, int weight);

    @Update("update user_medical_history set family_history=#{familyHistory},allergy_history=#{allergyHistory},past_medical_history=#{pastMedicalHistory},surgical_history=#{surgicalHistory},medical_compliance=#{medicalCompliance} where user_id=#{userId}")
    void updateUserHistory(int userId, String familyHistory, String allergyHistory, String pastMedicalHistory, String surgicalHistory, String medicalCompliance);


//    @Delete("delete  from user_addr where addr_id=#{addrId}")
//    void deleteAddr(long addrId);
}

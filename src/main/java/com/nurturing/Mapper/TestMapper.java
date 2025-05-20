package com.nurturing.Mapper;


import com.nurturing.entity.SleepData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface TestMapper {

    @Select("select TestMessage from test where TestId=1")
    String getTestMessage();

    @Update("update test set TestMessage=#{testMessage} where TestId=1")
    void setTestMessage(String testMessage);

}

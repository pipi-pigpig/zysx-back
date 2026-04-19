package com.nurturing.Mapper;

import com.nurturing.entity.User;
import com.nurturing.vo.ParentInfoVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserMapper {
    User selectById(@Param("id") Long id);
    int insert(User user);
    int update(User user);
    int deleteById(@Param("id") Long id);

    // 根据子女ID查询关联的父母信息
    List<ParentInfoVO> selectParentsByChildId(@Param("childId") Long childId);
}
package com.nurturing.Mapper;

import com.nurturing.entity.ChildParentRelation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ChildParentRelationMapper {
    int insert(ChildParentRelation relation);
    int deleteByChildIdAndParentId(@Param("childId") Long childId, @Param("parentId") Long parentId);
    ChildParentRelation selectByChildIdAndParentId(@Param("childId") Long childId, @Param("parentId") Long parentId);
    List<ChildParentRelation> selectByChildId(@Param("childId") Long childId);
}
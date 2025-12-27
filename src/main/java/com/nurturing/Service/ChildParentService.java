package com.nurturing.Service;


import com.nurturing.vo.AddParentRequest;
import com.nurturing.vo.ParentInfoVO;

import java.util.List;

public interface ChildParentService {
    List<ParentInfoVO> getParents(Long childId);
    ParentInfoVO addParent(Long childId, AddParentRequest request);
    ParentInfoVO updateParent(Long childId, Long parentId, AddParentRequest request);
    boolean deleteParent(Long childId, Long parentId);
}
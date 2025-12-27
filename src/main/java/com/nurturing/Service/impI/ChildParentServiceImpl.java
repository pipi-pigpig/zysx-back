package com.nurturing.Service.impI;


import com.nurturing.Mapper.ChildMapper;
import com.nurturing.Mapper.ChildParentRelationMapper;
import com.nurturing.Mapper.UserMapper;
import com.nurturing.Service.ChildParentService;
import com.nurturing.entity.ChildParentRelation;
import com.nurturing.entity.User;
import com.nurturing.vo.AddParentRequest;
import com.nurturing.vo.ParentInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ChildParentServiceImpl implements ChildParentService {

    @Autowired
    private ChildMapper childMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private ChildParentRelationMapper childParentRelationMapper;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public List<ParentInfoVO> getParents(Long childId) {
        return userMapper.selectParentsByChildId(childId);
    }

    @Override
    @Transactional
    public ParentInfoVO addParent(Long childId, AddParentRequest request) {
        // 1. 验证子女是否存在
        if (childMapper.selectById(childId) == null) {
            throw new RuntimeException("子女不存在");
        }

        // 2. 创建父母用户
        User parent = new User();
        parent.setUsername(request.getUsername());
        parent.setAccount(generateParentAccount(request.getPhone())); // 使用电话生成账号
        parent.setPassword(DigestUtils.md5DigestAsHex("default123".getBytes())); // 默认密码
        parent.setPhone(request.getPhone());
        parent.setAvatar(request.getAvatar());
        parent.setGender("M".equals(request.getGender()) ? "male" :
                "F".equals(request.getGender()) ? "female" : "other");
        parent.setBirthDate(LocalDate.parse(request.getBirthDate(), DATE_FORMATTER));
        parent.setHeight(request.getHeight());
        parent.setWeight(request.getWeight());

        userMapper.insert(parent);

        // 3. 建立关联关系
        ChildParentRelation relation = new ChildParentRelation();
        relation.setChildId(childId);
        relation.setParentId(parent.getId());
        relation.setRelationship("父母"); // 默认关系
        relation.setIsPrimary(0); // 默认为非主要监护人

        childParentRelationMapper.insert(relation);

        // 4. 返回父母信息
        return convertToParentInfoVO(parent);
    }

    @Override
    @Transactional
    public ParentInfoVO updateParent(Long childId, Long parentId, AddParentRequest request) {
        // 1. 验证关联关系是否存在
        ChildParentRelation relation = childParentRelationMapper.selectByChildIdAndParentId(childId, parentId);
        if (relation == null) {
            throw new RuntimeException("该父母不属于此子女");
        }

        // 2. 更新父母信息
        User parent = userMapper.selectById(parentId);
        if (parent == null) {
            throw new RuntimeException("父母不存在");
        }

        if (request.getUsername() != null) parent.setUsername(request.getUsername());
        if (request.getPhone() != null) parent.setPhone(request.getPhone());
        if (request.getAvatar() != null) parent.setAvatar(request.getAvatar());
        if (request.getGender() != null) {
            parent.setGender("M".equals(request.getGender()) ? "male" :
                    "F".equals(request.getGender()) ? "female" : "other");
        }
        if (request.getBirthDate() != null) {
            parent.setBirthDate(LocalDate.parse(request.getBirthDate(), DATE_FORMATTER));
        }
        if (request.getHeight() != null) parent.setHeight(request.getHeight());
        if (request.getWeight() != null) parent.setWeight(request.getWeight());

        userMapper.update(parent);

        // 3. 返回更新后的信息
        return convertToParentInfoVO(parent);
    }

    @Override
    @Transactional
    public boolean deleteParent(Long childId, Long parentId) {
        // 验证关联关系是否存在
        ChildParentRelation relation = childParentRelationMapper.selectByChildIdAndParentId(childId, parentId);
        if (relation == null) {
            throw new RuntimeException("该父母不属于此子女");
        }

        // 删除关联关系（由于外键设置了on delete cascade，会自动删除用户）
        int result = childParentRelationMapper.deleteByChildIdAndParentId(childId, parentId);
        return result > 0;
    }

    private String generateParentAccount(String phone) {
        return "parent_" + phone + "_" + System.currentTimeMillis();
    }

    private ParentInfoVO convertToParentInfoVO(User user) {
        ParentInfoVO vo = new ParentInfoVO();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setBirthDate(user.getBirthDate());
        vo.setGender(user.getGender());
        vo.setHeight(user.getHeight());
        vo.setWeight(user.getWeight());
        vo.setPhone(user.getPhone());
        vo.setAvatar(user.getAvatar());
        // 不再手动设置 lastActive，由数据库查询返回
        return vo;
    }
}
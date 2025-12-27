package com.nurturing.Service.impI;


import com.nurturing.Mapper.ChildMapper;
import com.nurturing.Mapper.UserMapper;
import com.nurturing.Service.ChildAuthService;
import com.nurturing.entity.Child;
import com.nurturing.vo.LoginRequest;
import com.nurturing.vo.LoginResponse;
import com.nurturing.vo.ParentInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ChildAuthServiceImpl implements ChildAuthService {

    @Autowired
    private ChildMapper childMapper;

    @Autowired
    private UserMapper userMapper;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public LoginResponse login(LoginRequest request) {
        LoginResponse response = new LoginResponse();

        // 1. 验证账号是否存在
        Child child = childMapper.selectByAccount(request.getAccount());
        if (child == null) {
            response.setCode(0);
            response.setMsg("账号不存在");
            return response;
        }

//        // 2. 验证密码（这里使用简单的MD5比较，实际项目中应该使用BCrypt等安全加密）
//        String encryptedPassword = DigestUtils.md5DigestAsHex(request.getPassword().getBytes());
//        if (!child.getPassword().equals(encryptedPassword)) {
//            response.setCode(0);
//            response.setMsg("密码错误");
//            return response;
//        }

        // 3. 获取关联的父母信息
        List<ParentInfoVO> parents = userMapper.selectParentsByChildId(child.getId());

        // 4. 生成token（这里简单实现，实际应该使用JWT）
        String token = "child_token_" + child.getId() + "_" + System.currentTimeMillis();

        // 5. 构造响应数据
        response.setCode(1);
        response.setMsg("登录成功");

        LoginResponse.Data data = new LoginResponse.Data();
        data.setToken(token);

        // 子女信息
        LoginResponse.ChildInfo childInfo = new LoginResponse.ChildInfo();
        childInfo.setId(child.getId());
        childInfo.setAccount(child.getAccount());
        childInfo.setUsername(child.getUsername());
        childInfo.setPhone(child.getPhone());
        childInfo.setAvatar(child.getAvatar());
        childInfo.setGender(child.getGender());
        childInfo.setBirthDate(child.getBirthDate() != null ?
                child.getBirthDate().format(DATE_FORMATTER) : null);
        childInfo.setRelationship(child.getRelationship());
        childInfo.setEmail(child.getEmail());
        data.setChildInfo(childInfo);

        // 父母信息
        data.setParents(parents);

        response.setData(data);

        return response;
    }
}
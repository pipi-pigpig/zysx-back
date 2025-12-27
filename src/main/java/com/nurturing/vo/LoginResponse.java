package com.nurturing.vo;

import com.nurturing.vo.ParentInfoVO;
import lombok.Data;
import java.util.List;

@Data
public class LoginResponse {
    private Integer code;
    private String msg;
    private Data data;

    @lombok.Data
    public static class Data {
        private String token;
        private ChildInfo childInfo;
        private List<ParentInfoVO> parents;
    }

    @lombok.Data
    public static class ChildInfo {
        private Long id;
        private String account;
        private String username;
        private String phone;
        private String avatar;
        private String gender;
        private String birthDate;
        private String relationship;
        private String email;
    }
}
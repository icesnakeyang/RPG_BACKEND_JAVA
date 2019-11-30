package com.gogoyang.rpgapi.controller.user;

import lombok.Data;

@Data
public class UserRequest {
    private String email;
    private String phone;
    private String password;
    private String loginName;
    private String realName;
    private String code;
}

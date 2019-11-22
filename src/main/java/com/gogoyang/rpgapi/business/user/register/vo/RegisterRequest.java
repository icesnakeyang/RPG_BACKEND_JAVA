package com.gogoyang.rpgapi.business.user.register.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String email;
    private String loginPassword;
    private String realName;
    private String phone;
    private String code;
}

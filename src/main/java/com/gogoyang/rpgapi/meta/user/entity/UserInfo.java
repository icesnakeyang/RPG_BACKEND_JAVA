package com.gogoyang.rpgapi.meta.user.entity;

import lombok.Data;

import java.util.Date;

@Data
public class UserInfo {
    private Integer ids;
    private String userId;
    private String loginPassword;
    private String token;
    private Date registerTime;
    private Date tokenCreatedTime;
    private Double account;
    private Double accountIn;
    private Double accountOut;
    private Integer honor;
    private Integer honorIn;
    private Integer honorOut;
    private String email;
    private String phone;
    private String realName;
}

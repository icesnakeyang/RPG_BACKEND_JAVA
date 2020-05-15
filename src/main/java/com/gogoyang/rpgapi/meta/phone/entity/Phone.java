package com.gogoyang.rpgapi.meta.phone.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Phone {
    private Integer ids;
    private String phoneId;
    private String userId;
    private Date createdTime;
    private String createdUserId;
    private String phone;
    private Boolean isDefault;
    private Boolean isVerify;
    private Date verifyTime;
}

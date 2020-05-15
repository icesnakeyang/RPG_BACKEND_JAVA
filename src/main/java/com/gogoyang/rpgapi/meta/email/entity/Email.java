package com.gogoyang.rpgapi.meta.email.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Email {
    private Integer ids;
    private String emailId;
    private String userId;
    private String email;
    private Date createdTime;
    private String createdUserId;
}

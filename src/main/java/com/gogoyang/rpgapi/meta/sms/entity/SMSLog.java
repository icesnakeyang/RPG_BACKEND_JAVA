package com.gogoyang.rpgapi.meta.sms.entity;

import lombok.Data;

import java.util.Date;

@Data
public class SMSLog {
    private Integer ids;
    private String phone;
    private String code;
    private Date createTime;
    private String status;
}

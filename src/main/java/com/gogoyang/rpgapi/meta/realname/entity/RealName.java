package com.gogoyang.rpgapi.meta.realname.entity;

import lombok.Data;

import java.util.Date;

@Data
public class RealName {
    private Integer ids;
    private String userId;
    private String realName;
    private Date createdTime;
    private String status;
    private String idcardNo;
    private String sex;
    private String verify;
    private String remark;
}

package com.gogoyang.rpgapi.meta.userAction.entity;

import lombok.Data;

import java.util.Date;

@Data
public class UserActionLog {
    private Integer ids;

    private String userId;

    private Date createTime;

    private String actType;

    private String memo;

    private String result;
}

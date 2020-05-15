package com.gogoyang.rpgapi.meta.account.entity;

import com.gogoyang.rpgapi.framework.constant.AccountType;
import lombok.Data;

import java.util.Date;

/**
 * 用户账户表
 */
@Data
public class Account {
    private Integer ids;
    private String accountId;
    private String userId;
    private Date createdTime;
    private Double amount;
    private String type;
    private String remark;
    private String jobId;

    private String userName;
    private String jobTitle;
}

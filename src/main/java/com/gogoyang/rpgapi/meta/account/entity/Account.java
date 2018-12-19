package com.gogoyang.rpgapi.meta.account.entity;

import com.gogoyang.rpgapi.framework.constant.AccountType;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Account {
    @Id
    @GeneratedValue
    @Column(name = "account_id")
    private Integer accountId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "created_time")
    private Date createdTime;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "type")
    private AccountType type;

    @Column(name = "remark")
    private String remark;

    @Transient
    private String cratedTimeStr;

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
        this.type = type;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCratedTimeStr() {
        return cratedTimeStr;
    }

    public void setCratedTimeStr(String cratedTimeStr) {
        this.cratedTimeStr = cratedTimeStr;
    }
}

package com.gogoyang.rpgapi.topUp.entity;

import com.gogoyang.rpgapi.constant.AccountType;
import com.gogoyang.rpgapi.constant.LogStatus;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class TopUp {
    @Id
    @GeneratedValue
    @Column(name = "topup_id")
    private Integer topUpId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "type")
    private AccountType type;

    @Column(name = "remark")
    private String remark;

    @Column(name = "read_time")
    private Date readTime;

    @Column(name = "process_result")
    private LogStatus processResult;

    @Column(name = "process_user_id")
    private Integer processUserId;

    @Column(name = "process_time")
    private Date processTime;

    @Column(name = "process_remark")
    private String processRemark;

    /////////////////////////////////////////////////////////////////////////////////////////////////////

    public Integer getTopUpId() {
        return topUpId;
    }

    public void setTopUpId(Integer topUpId) {
        this.topUpId = topUpId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public Date getReadTime() {
        return readTime;
    }

    public void setReadTime(Date readTime) {
        this.readTime = readTime;
    }

    public LogStatus getProcessResult() {
        return processResult;
    }

    public void setProcessResult(LogStatus processResult) {
        this.processResult = processResult;
    }

    public Integer getProcessUserId() {
        return processUserId;
    }

    public void setProcessUserId(Integer processUserId) {
        this.processUserId = processUserId;
    }

    public Date getProcessTime() {
        return processTime;
    }

    public void setProcessTime(Date processTime) {
        this.processTime = processTime;
    }

    public String getProcessRemark() {
        return processRemark;
    }

    public void setProcessRemark(String processRemark) {
        this.processRemark = processRemark;
    }
}

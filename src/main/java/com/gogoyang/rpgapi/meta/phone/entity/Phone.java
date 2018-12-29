package com.gogoyang.rpgapi.meta.phone.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Phone {
    @Id
    @GeneratedValue
    @Column(name = "phone_id")
    private Integer phoneId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "created_time")
    private Date createdTime;

    @Column(name = "created_user_id")
    private Integer createdUserId;

    @Column(name = "phone")
    private String phone;

    @Column(name = "is_default")
    private Boolean isDefault;

    @Column(name = "is_verify")
    private Boolean isVerify;

    @Column(name = "send_verify_time")
    private Date sendVerifyTime;

    @Column(name = "verify_code")
    private String verifyCode;

    @Column(name = "verify_time")
    private Date verifyTime;
    //////////////////////////////////////////////////////////////////////////////////////////

    public Integer getPhoneId() {
        return phoneId;
    }

    public void setPhoneId(Integer phoneId) {
        this.phoneId = phoneId;
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

    public Integer getCreatedUserId() {
        return createdUserId;
    }

    public void setCreatedUserId(Integer createdUserId) {
        this.createdUserId = createdUserId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    public Boolean getVerify() {
        return isVerify;
    }

    public void setVerify(Boolean verify) {
        isVerify = verify;
    }

    public Date getSendVerifyTime() {
        return sendVerifyTime;
    }

    public void setSendVerifyTime(Date sendVerifyTime) {
        this.sendVerifyTime = sendVerifyTime;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public Date getVerifyTime() {
        return verifyTime;
    }

    public void setVerifyTime(Date verifyTime) {
        this.verifyTime = verifyTime;
    }
}

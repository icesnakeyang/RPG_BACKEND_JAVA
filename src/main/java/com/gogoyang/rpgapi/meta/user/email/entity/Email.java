package com.gogoyang.rpgapi.meta.user.email.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class Email {
    @Id
    @GeneratedValue
    @Column(name = "ids")
    private Integer ids;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "email")
    private String email;

    @Column(name = "created_time")
    private Date createdTime;

    @Column(name = "created_user_id")
    private Integer createdUserId;

    @Column(name = "is_default")
    private Boolean isDefault;

    @Column(name = "is_verified")
    private Boolean isVerified;

    @Column(name = "verify_key")
    private String verifyKey;

    @Column(name = "send_verify_time")
    private Date sendVerifyTime;

    @Column(name = "verify_time")
    private Date verifyTime;


    /////////////////////////////////////////////////////////////////////////////////////////////////////
    public Integer getIds() {
        return ids;
    }

    public void setIds(Integer ids) {
        this.ids = ids;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public Boolean getDefault() {
        return isDefault;
    }

    public void setDefault(Boolean aDefault) {
        isDefault = aDefault;
    }

    public Boolean getVerified() {
        return isVerified;
    }

    public void setVerified(Boolean verified) {
        isVerified = verified;
    }

    public String getVerifyKey() {
        return verifyKey;
    }

    public void setVerifyKey(String verifyKey) {
        this.verifyKey = verifyKey;
    }

    public Date getSendVerifyTime() {
        return sendVerifyTime;
    }

    public void setSendVerifyTime(Date sendVerifyTime) {
        this.sendVerifyTime = sendVerifyTime;
    }

    public Date getVerifyTime() {
        return verifyTime;
    }

    public void setVerifyTime(Date verifyTime) {
        this.verifyTime = verifyTime;
    }
}

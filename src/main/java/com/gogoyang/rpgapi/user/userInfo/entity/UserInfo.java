package com.gogoyang.rpgapi.user.userInfo.entity;

import javax.persistence.*;
import java.util.Date;

/**
 * UserInfo类定义了用户的各种当前有效信息
 */
@Entity
public class UserInfo {
    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "register_time")
    private Date registerTime;

    @Column(name = "token")
    private String token;

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "real_name")
    private String realName;

    @Column(name = "account")
    private Double account;

    @Column(name = "account_in")
    private Double accountIn;

    @Column(name = "account_out")
    private Double accountOut;

    @Column(name = "honor")
    private Double honor;

    @Column(name = "honor_in")
    private Double honorIn;

    @Column(name = "honor_out")
    private Double honorOut;


    ////////////////////////////////////////////////////////////////////////////////////////////////
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Double getAccount() {
        return account;
    }

    public void setAccount(Double account) {
        this.account = account;
    }

    public Double getAccountIn() {
        return accountIn;
    }

    public void setAccountIn(Double accountIn) {
        this.accountIn = accountIn;
    }

    public Double getAccountOut() {
        return accountOut;
    }

    public void setAccountOut(Double accountOut) {
        this.accountOut = accountOut;
    }

    public Double getHonor() {
        return honor;
    }

    public void setHonor(Double honor) {
        this.honor = honor;
    }

    public Double getHonorIn() {
        return honorIn;
    }

    public void setHonorIn(Double honorIn) {
        this.honorIn = honorIn;
    }

    public Double getHonorOut() {
        return honorOut;
    }

    public void setHonorOut(Double honorOut) {
        this.honorOut = honorOut;
    }
}

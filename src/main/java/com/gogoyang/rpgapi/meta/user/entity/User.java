package com.gogoyang.rpgapi.meta.user.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class User {
    @Id
    @GeneratedValue
    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "login_password")
    private String loginPassword;

    @Column(name = "token")
    private String token;

    @Column(name = "register_time")
    private Date registerTime;

    @Column(name = "token_created_time")
    private Date tokenCreatedTime;

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

    @Column(name = "email")
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "real_name")
    private String realName;
    //////////////////////////////////////////////////////////////////////////////////////////

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(Date registerTime) {
        this.registerTime = registerTime;
    }

    public Date getTokenCreatedTime() {
        return tokenCreatedTime;
    }

    public void setTokenCreatedTime(Date tokenCreatedTime) {
        this.tokenCreatedTime = tokenCreatedTime;
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
}

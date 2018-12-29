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
    private Integer honor;

    @Column(name = "honor_in")
    private Integer honorIn;

    @Column(name = "honor_out")
    private Integer honorOut;
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

    public Integer getHonor() {
        return honor;
    }

    public void setHonor(Integer honor) {
        this.honor = honor;
    }

    public Integer getHonorIn() {
        return honorIn;
    }

    public void setHonorIn(Integer honorIn) {
        this.honorIn = honorIn;
    }

    public Integer getHonorOut() {
        return honorOut;
    }

    public void setHonorOut(Integer honorOut) {
        this.honorOut = honorOut;
    }
}

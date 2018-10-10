package com.gogoyang.rpgapi.meta.user.realName.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class RealName {
    @Id
    @GeneratedValue
    @Column(name = "real_name_id")
    private Integer realNameId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "real_name")
    private String realName;

    @Column(name = "created_time")
    private Date createdTime;

    @Column(name = "disable_time")
    private Boolean disableTime;

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////


    public Integer getRealNameId() {
        return realNameId;
    }

    public void setRealNameId(Integer realNameId) {
        this.realNameId = realNameId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Boolean getDisableTime() {
        return disableTime;
    }

    public void setDisableTime(Boolean disableTime) {
        this.disableTime = disableTime;
    }
}

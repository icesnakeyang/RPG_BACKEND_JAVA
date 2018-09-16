package com.gogoyang.rpgapi.user.entity;

import com.gogoyang.rpgapi.constant.RoleType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity
public class RoleUser {
    @Id
    @GeneratedValue
    @Column(name = "ids")
    private Integer ids;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "role")
    private RoleType userRole;

    @Column(name = "created_time")
    private Date createdTime;

    @Column(name = "created_user_id")
    private Integer createdUserId;

    @Column(name = "disable")
    private boolean disable;

    @Column(name = "disable_time")
    private Date disableTime;

    @Column(name = "disable_user_id")
    private Integer disableUserId;

    @Column(name = "disable_remark")
    private String disableRemark;

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

    public RoleType getUserRole() {
        return userRole;
    }

    public void setUserRole(RoleType userRole) {
        this.userRole = userRole;
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

    public boolean isDisable() {
        return disable;
    }

    public void setDisable(boolean disable) {
        this.disable = disable;
    }

    public Date getDisableTime() {
        return disableTime;
    }

    public void setDisableTime(Date disableTime) {
        this.disableTime = disableTime;
    }

    public Integer getDisableUserId() {
        return disableUserId;
    }

    public void setDisableUserId(Integer disableUserId) {
        this.disableUserId = disableUserId;
    }

    public String getDisableRemark() {
        return disableRemark;
    }

    public void setDisableRemark(String disableRemark) {
        this.disableRemark = disableRemark;
    }
}

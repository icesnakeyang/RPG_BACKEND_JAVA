package com.gogoyang.rpgapi.admin.entity;

import com.gogoyang.rpgapi.constant.RoleType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

/**
 * Admin实体定义管理员类型的用户
 * 管理员包括ROOT_ADMIN, SUPER_ADMIN, ADMINISTRATOR, SECRETARY
 */
@Entity
public class Admin {
    @Id
    @GeneratedValue
    @Column(name = "admin_id")
    private Integer adminId;

    /**
     * 用于登录的用户名，唯一，不重复
     */
    @Column(name = "login_name")
    private String loginName;

    /**
     * 用于登录的密码
     */
    @Column(name = "password")
    private String password;

    @Column(name = "token")
    private String token;

    /**
     * 用户权限类型
     */
    @Column(name = "role_type")
    private RoleType roleType;

    /**
     * 创建该用户的时间
     */
    @Column(name = "created_time")
    private Date createdTime;

    /**
     * 创建该用户的上级用户Id
     */
    @Column(name = "created_admin_id")
    private Integer createdAdminId;

    //////////////////////////////////////////////////////////////////////////////////////////

    public Integer getAdminId() {
        return adminId;
    }

    public void setAdminId(Integer adminId) {
        this.adminId = adminId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Integer getCreatedAdminId() {
        return createdAdminId;
    }

    public void setCreatedAdminId(Integer createdAdminId) {
        this.createdAdminId = createdAdminId;
    }
}

package com.gogoyang.rpgapi.meta.admin.entity;

import lombok.Data;

import java.util.Date;

/**
 * Admin实体定义管理员类型的用户
 * 管理员包括ROOT_ADMIN, SUPER_ADMIN, ADMINISTRATOR, SECRETARY
 */
@Data
public class Admin {
    private Integer ids;
    private String adminId;
    private String loginName;
    private String password;
    private String token;
    private Date tokenTime;
    private String roleType;
    private Date createdTime;
    private String createdAdminId;
    private String phone;
    private String adminName;
}

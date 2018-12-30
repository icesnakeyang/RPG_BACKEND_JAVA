package com.gogoyang.rpgapi.meta.admin.service;

import com.gogoyang.rpgapi.meta.admin.entity.Admin;
import com.gogoyang.rpgapi.framework.constant.RoleType;

import java.util.ArrayList;

public interface IAdminService {
    Admin loadAdminByLoginName(String loginName) throws Exception;

    Admin createAdmin(Admin admin) throws Exception;

    Admin loadAdminByToken(String token) throws Exception;

    ArrayList<Admin> loadAdminByRoleType(RoleType roleType) throws Exception;
}

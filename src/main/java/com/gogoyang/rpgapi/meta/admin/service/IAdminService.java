package com.gogoyang.rpgapi.meta.admin.service;

import com.gogoyang.rpgapi.meta.admin.entity.Admin;
import com.gogoyang.rpgapi.framework.constant.RoleType;

import java.util.ArrayList;

public interface IAdminService {
    Admin getAdminByLoginName(String loginName) throws Exception;

    Admin createAdmin(Admin admin) throws Exception;

    /**根据token读取管理员用户信息
     *
     * @param token
     * @return
     * @throws Exception
     */
    Admin getAdminByToken(String token) throws Exception;

    ArrayList<Admin> listAdminByRoleType(RoleType roleType) throws Exception;

    Admin getAdminByPhone(String phone) throws Exception;

    void updateAdmin(Admin admin) throws Exception;

    void deleteAdmin(String adminId) throws Exception;
}

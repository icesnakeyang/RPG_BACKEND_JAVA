package com.gogoyang.rpgapi.meta.admin.service;

import com.gogoyang.rpgapi.meta.admin.dao.AdminDao;
import com.gogoyang.rpgapi.meta.admin.entity.Admin;
import com.gogoyang.rpgapi.framework.constant.RoleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class AdminService implements IAdminService {
    private final AdminDao adminDao;

    @Autowired
    public AdminService(AdminDao adminDao) {
        this.adminDao = adminDao;
    }

    /**
     * 根据loginName读取Admin
     *
     * @param loginName
     * @return
     * @throws Exception
     */
    @Override
    public Admin getAdminByLoginName(String loginName) throws Exception {
        /**
         * adminDao.findByLoginName
         */
        Map qIn=new HashMap();
        qIn.put("loginName", loginName);
        Admin admin = adminDao.getAdmin(qIn);
        return admin;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Admin createAdmin(Admin admin) throws Exception {
        /**
         * adminDao.save(admin)
         */
        adminDao.createAdmin(admin);
        return admin;
    }

    /**
     * 根据token读取管理员用户信息
     * @param token
     * @return
     * @throws Exception
     */
    @Override
    public Admin getAdminByToken(String token) throws Exception {
        Map qIn=new HashMap();
        qIn.put("token", token);
        Admin admin = adminDao.getAdmin(qIn);
        return admin;
    }

    /**
     * 根据用户类型读取所有管理员用户
     *
     * @param roleType
     * @return
     * @throws Exception
     */
    @Override
    public ArrayList<Admin> listAdminByRoleType(RoleType roleType) throws Exception {
        Map qIn=new HashMap();
        qIn.put("roleType",roleType);
        ArrayList<Admin> admins = adminDao.listAdmin(qIn);
        return admins;
    }

    @Override
    public Admin getAdminByPhone(String phone) throws Exception {
        Map qIn=new HashMap();
        qIn.put("phone", phone);
        Admin admin= adminDao.getAdmin(qIn);
        return admin;
    }

    @Override
    public void updateAdmin(Admin admin) throws Exception {
        adminDao.updateAdmin(admin);
    }

    @Override
    public void deleteAdmin(String adminId) throws Exception {
        adminDao.deleteAdmin(adminId);
    }

}

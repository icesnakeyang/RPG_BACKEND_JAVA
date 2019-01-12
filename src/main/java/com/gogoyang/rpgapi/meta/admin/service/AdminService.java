package com.gogoyang.rpgapi.meta.admin.service;

import com.gogoyang.rpgapi.meta.admin.dao.AdminDao;
import com.gogoyang.rpgapi.meta.admin.entity.Admin;
import com.gogoyang.rpgapi.framework.constant.RoleType;
import com.gogoyang.rpgapi.meta.apply.service.IJobApplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;

@Service
public class AdminService implements IAdminService {
    private final AdminDao adminDao;
    private final IJobApplyService iJobApplyService;

    @Autowired
    public AdminService(AdminDao adminDao,
                        IJobApplyService iJobApplyService) {
        this.adminDao = adminDao;
        this.iJobApplyService=iJobApplyService;

    }

    /**
     * 根据loginName读取Admin
     *
     * @param loginName
     * @return
     * @throws Exception
     */
    @Override
    public Admin loadAdminByLoginName(String loginName) throws Exception {
        /**
         * adminDao.findByLoginName
         */
        Admin admin = adminDao.findByLoginName(loginName);
        return admin;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Admin createAdmin(Admin admin) throws Exception {
        /**
         * adminDao.save(admin)
         */
        admin.setAdminId(adminDao.save(admin).getAdminId());
        return admin;
    }

    @Override
    public Admin loadAdminByToken(String token) throws Exception {
        Admin admin = adminDao.findByToken(token);
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
    public ArrayList<Admin> loadAdminByRoleType(RoleType roleType) throws Exception {
        ArrayList<Admin> admins = adminDao.findAllByRoleType(roleType);
        return admins;
    }


}

package com.gogoyang.rpgapi.meta.admin.service;

import com.gogoyang.rpgapi.meta.admin.dao.AdminDao;
import com.gogoyang.rpgapi.meta.admin.entity.Admin;
import com.gogoyang.rpgapi.framework.constant.RoleType;
import com.gogoyang.rpgapi.meta.apply.entity.JobApply;
import com.gogoyang.rpgapi.meta.apply.service.IJobApplyService;
import com.gogoyang.rpgapi.meta.match.entity.JobMatch;
import com.gogoyang.rpgapi.meta.match.service.IJobMatchService;
import com.gogoyang.rpgapi.meta.user.userInfo.entity.UserInfo;
import com.gogoyang.rpgapi.meta.user.userInfo.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class AdminService implements IAdminService {
    private final AdminDao adminDao;
    private final IJobMatchService iJobMatchService;
    private final IJobApplyService iJobApplyService;
    private final IUserInfoService iUserInfoService;

    @Autowired
    public AdminService(AdminDao adminDao, IJobMatchService iJobMatchService,
                        IJobApplyService iJobApplyService,
                        IUserInfoService iUserInfoService) {
        this.adminDao = adminDao;
        this.iJobMatchService = iJobMatchService;
        this.iJobApplyService=iJobApplyService;
        this.iUserInfoService=iUserInfoService;
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

package com.gogoyang.rpgapi.business.admin.admin.service;

import com.gogoyang.rpgapi.framework.common.IRPGFunction;
import com.gogoyang.rpgapi.framework.constant.LogStatus;
import com.gogoyang.rpgapi.framework.constant.RoleType;
import com.gogoyang.rpgapi.meta.admin.entity.Admin;
import com.gogoyang.rpgapi.meta.admin.service.IAdminService;
import com.gogoyang.rpgapi.meta.sms.entity.SMSLog;
import com.gogoyang.rpgapi.meta.sms.service.ISMSLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.relation.Role;
import javax.transaction.Transactional;
import java.util.*;

@Service
public class AdminBusinessService implements IAdminBusinessService {
    private final IRPGFunction irpgFunction;
    private final IAdminService iAdminService;
    private final ISMSLogService ismsLogService;

    @Autowired
    public AdminBusinessService(IRPGFunction irpgFunction,
                                IAdminService iAdminService,
                                ISMSLogService ismsLogService) {
        this.irpgFunction = irpgFunction;
        this.iAdminService = iAdminService;
        this.ismsLogService = ismsLogService;
    }

    /**
     * 创建rootAdmin用户的核心业务逻辑
     * The core business logic of create a admin user is here
     *
     * @param in
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public Map createRootAdmin(Map in) throws Exception {
        Map out = new HashMap();

        Admin rootAdmin = new Admin();
        String loginName = in.get("loginName").toString();
        String password = in.get("password").toString();
        RoleType roleType = (RoleType) in.get("roleType");
        rootAdmin.setLoginName(loginName);
        rootAdmin.setPassword(password);
        rootAdmin.setCreatedTime(new Date());
        rootAdmin.setPassword(irpgFunction.encoderByMd5(rootAdmin.getPassword()));
        rootAdmin.setRoleType(roleType);
        rootAdmin.setToken(UUID.randomUUID().toString().replace("-", ""));
        rootAdmin = iAdminService.createAdmin(rootAdmin);

        out.put("admin", rootAdmin);
        return out;
    }

    /**
     * 创建一个superAdmin用户
     *
     * @param in
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public Map createSuperAdmin(Map in) throws Exception {
        String loginName = in.get("loginName").toString();
        String token = in.get("token").toString();
        String password = in.get("password").toString();

        //whether the loginName duplicated
        Admin admin = iAdminService.getAdminByLoginName(loginName);
        if (admin != null) {
            throw new Exception("10016");
        }

        //check current admin user permission, only rootAdmin can create superAdmin
        Admin rootAdmin = iAdminService.getAdminByToken(token);
        if (rootAdmin == null) {
            throw new Exception("10004");
        }
        if (rootAdmin.getRoleType().ordinal() != RoleType.ROOT_ADMIN.ordinal()) {
            throw new Exception("10008");
        }

        Admin newAdmin = new Admin();
        newAdmin.setCreatedTime(new Date());
        newAdmin.setLoginName(loginName);
        newAdmin.setPassword(password);
        newAdmin.setPassword(irpgFunction.encoderByMd5(newAdmin.getPassword()));
        newAdmin.setRoleType(RoleType.SUPER_ADMIN);
        newAdmin.setToken(UUID.randomUUID().toString().replace("-", ""));
        newAdmin = iAdminService.createAdmin(newAdmin);

        Map out = new HashMap();
        out.put("admin", newAdmin);
        return out;
    }

    /**
     * 创建Admin用户的业务逻辑
     *
     * @param in
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public Map createAdministrator(Map in) throws Exception {
        String loginName = in.get("loginName").toString();
        String token = in.get("token").toString();
        String password = in.get("password").toString();

        //check whether the loginName duplicated
        Admin admin = iAdminService.getAdminByLoginName(loginName);
        if (admin != null) {
            throw new Exception("10016");
        }

        //check current operator permission, only superAdmin can create administrator and secretary
        Admin superAdmin = iAdminService.getAdminByToken(token);
        if (superAdmin == null) {
            throw new Exception("10004");
        }
        if (!(superAdmin.getRoleType().ordinal() < RoleType.SUPER_ADMIN.ordinal())) {
            throw new Exception("10008");
        }
        Admin newAdmin = new Admin();
        newAdmin.setCreatedTime(new Date());
        newAdmin.setLoginName(loginName);
        newAdmin.setPassword(password);
        newAdmin.setPassword(irpgFunction.encoderByMd5(newAdmin.getPassword()));
        newAdmin.setRoleType(RoleType.ADMINISTRATOR);
        newAdmin.setToken(UUID.randomUUID().toString().replace("-", ""));
        newAdmin = iAdminService.createAdmin(newAdmin);

        Map out = new HashMap();
        out.put("admin", newAdmin);
        return out;
    }

    /**
     * 管理员账户登录
     *
     * @param in
     * @return
     * @throws Exception
     */
    @Override
    public Map login(Map in) throws Exception {
        String loginName = in.get("loginName").toString();
        String password = in.get("password").toString();
        Admin admin = iAdminService.getAdminByLoginName(loginName);
        if (admin == null) {
            throw new Exception("10024");
        }
        if (!irpgFunction.encoderByMd5(password).equals(admin.getPassword())) {
            throw new Exception("10024");
        }
        Map out = new HashMap();
        out.put("admin", admin);
        return out;
    }

    /**
     * 读取所有比当前用户权限低一级的管理员用户
     * read all administrators
     *
     * @param in
     * @return
     * @throws Exception
     */
    @Override
    public Map loadAdmin(Map in) throws Exception {
        String token = in.get("token").toString();

        Admin admin = iAdminService.getAdminByToken(token);
        if (admin == null) {
            throw new Exception("10004");
        }
        ArrayList adminList = new ArrayList();

        if (admin.getRoleType().ordinal() < RoleType.SECRETARY.ordinal()) {
            ArrayList<Admin> list = iAdminService.listAdminByRoleType(RoleType.SECRETARY);
            if (list.size() > 0) {
                Map map = new HashMap();
                map.put("role", RoleType.SECRETARY);
                map.put("admin", list);
                adminList.add(map);
            }
        }
        if (admin.getRoleType().ordinal() < RoleType.ADMINISTRATOR.ordinal()) {
            ArrayList<Admin> list = iAdminService.listAdminByRoleType(RoleType.ADMINISTRATOR);
            if (list.size() > 0) {
                Map map = new HashMap();
                map.put("role", RoleType.ADMINISTRATOR);
                map.put("admin", list);
                adminList.add(map);
            }
        }
        if (admin.getRoleType().ordinal() < RoleType.SUPER_ADMIN.ordinal()) {
            ArrayList<Admin> list = iAdminService.listAdminByRoleType(RoleType.SUPER_ADMIN);
            if (list.size() > 0) {
                Map map = new HashMap();
                map.put("role", RoleType.SUPER_ADMIN);
                map.put("admin", list);
                adminList.add(map);
            }
        }
        Map out = new HashMap();
        out.put("admins", adminList);
        return out;
    }

    @Override
    public Map listRoleTypes() throws Exception {
        return null;
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public void resetPassword(Map in) throws Exception {
        String code = in.get("code").toString();
        String phone = in.get("phone").toString();
        String newPassword = in.get("newPassword").toString();

        irpgFunction.verifyMSMCode(phone, code);

        /**
         * 修改密码
         */
        Admin admin = iAdminService.getAdminByPhone(phone);
        admin.setPassword(irpgFunction.encoderByMd5(newPassword));
        iAdminService.updateAdmin(admin);

        /**
         * 把smslog设置失效
         */
        SMSLog smsLog=ismsLogService.getSMSLog(phone,code);
        smsLog.setStatus(LogStatus.OVERDUE.toString());
        ismsLogService.updateSMSLog(smsLog);
    }
}

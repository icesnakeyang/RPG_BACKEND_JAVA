package com.gogoyang.rpgapi.business.admin.admin.service;

import com.gogoyang.rpgapi.framework.common.IRPGFunction;
import com.gogoyang.rpgapi.framework.constant.RoleType;
import com.gogoyang.rpgapi.meta.admin.entity.Admin;
import com.gogoyang.rpgapi.meta.admin.service.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.relation.Role;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class AdminBusinessService implements IAdminBusinessService{
    private final IRPGFunction irpgFunction;
    private final IAdminService iAdminService;

    @Autowired
    public AdminBusinessService(IRPGFunction irpgFunction, IAdminService iAdminService) {
        this.irpgFunction = irpgFunction;
        this.iAdminService = iAdminService;
    }

    /**
     * 创建rootAdmin用户的核心业务逻辑
     * The core business logic of create a admin user is here
     * @param in
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public Map createRootAdmin(Map in) throws Exception {
        Map out=new HashMap();

        Admin rootAdmin = new Admin();
        String loginName=in.get("loginName").toString();
        String password=in.get("password").toString();
        RoleType roleType=(RoleType)in.get("roleType");
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
     * @param in
     * @return
     * @throws Exception
     */
    @Override
    public Map createSuperAdmin(Map in) throws Exception {
        String loginName=in.get("loginName").toString();
        String token=in.get("token").toString();
        String password=in.get("password").toString();

        //whether the loginName duplicated
        Admin admin=iAdminService.loadAdminByLoginName(loginName);
        if(admin!=null){
            throw new Exception("10016");
        }

        Admin rootAdmin=iAdminService.loadAdminByToken(token);
        if(rootAdmin==null){
            throw new Exception("10004");
        }
        if(rootAdmin.getRoleType().ordinal()!= RoleType.ROOT_ADMIN.ordinal()){
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

        Map out=new HashMap();
        out.put("admin", newAdmin);
        return out;
    }

    /**
     * 创建Admin用户的业务逻辑
     * @param in
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public Map createNewAdmin(Map in) throws Exception {
        String loginName=in.get("loginName").toString();
        String token=in.get("token").toString();
        String password=in.get("password").toString();

        try {
            //check whether the loginName duplicated
            Admin admin = iAdminService.loadAdminByLoginName(loginName);
            if (admin != null) {
                throw new Exception("10016");
            }

            //check current operator permission, only superAdmin can create administrator and secretary
            Admin superAdmin = iAdminService.loadAdminByToken(token);
            if (superAdmin == null) {
                throw new Exception("10004");
            }
            if (!(superAdmin.getRoleType().ordinal() < request.getRoleType().ordinal())) {
                response.setErrorCode(10008);
                return response;
            }
            //enum less, authority more high
            Admin newAdmin = new Admin();
            newAdmin.setCreatedTime(new Date());
            newAdmin.setLoginName(request.getLoginName());
            newAdmin.setPassword(request.getPassword());
            newAdmin.setPassword(irpgFunction.encoderByMd5(newAdmin.getPassword()));
            newAdmin.setRoleType(request.getRoleType());
            newAdmin.setToken(UUID.randomUUID().toString().replace("-", ""));
            newAdmin = iAdminService.createAdmin(newAdmin);

            response.setData(newAdmin);

        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
                return response;
            } catch (Exception ex2) {
                response.setErrorCode(10026);
                return response;
            }
        }

        return response;


        Map out=new HashMap();
        return out;
    }
}

package com.gogoyang.rpgapi.business.admin.admin.service;

import com.gogoyang.rpgapi.framework.common.IRPGFunction;
import com.gogoyang.rpgapi.framework.constant.RoleType;
import com.gogoyang.rpgapi.meta.admin.entity.Admin;
import com.gogoyang.rpgapi.meta.admin.service.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
     * 创建Admin用户的核心业务逻辑
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
}

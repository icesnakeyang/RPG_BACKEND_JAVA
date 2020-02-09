package com.gogoyang.rpgapi.business.admin.common;

import com.gogoyang.rpgapi.meta.admin.entity.Admin;
import com.gogoyang.rpgapi.meta.admin.service.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminCommonBusinessService implements IAdminCommonBusinessService {
    private final IAdminService iAdminService;

    public AdminCommonBusinessService(IAdminService iAdminService) {
        this.iAdminService = iAdminService;
    }

    @Override
    public Admin getAdminUserByToken(String token) throws Exception {
        Admin admin=iAdminService.getAdminByToken(token);
        if(admin==null){
            throw new Exception("20001");
        }
        return admin;
    }
}

package com.gogoyang.rpgapi.business.admin.admin.service;

import java.util.Map;

public interface IAdminBusinessService {
    Map createRootAdmin(Map in) throws Exception;
    Map createSuperAdmin(Map in) throws Exception;
    Map createAdministrator(Map in) throws Exception;
    Map login(Map in) throws Exception;
    Map loadAdmin(Map in) throws Exception;

    Map listRoleTypes() throws Exception;

    void getPhoneVerifyCode(Map in) throws Exception;
}

package com.gogoyang.rpgapi.business.admin.admin.service;

import com.gogoyang.rpgapi.meta.admin.entity.Admin;

import java.util.Map;

public interface IAdminBusinessService {
    Map createRootAdmin(Map in) throws Exception;
    Map createSuperAdmin(Map in) throws Exception;
    Map createNewAdmin(Map in) throws Exception;
}

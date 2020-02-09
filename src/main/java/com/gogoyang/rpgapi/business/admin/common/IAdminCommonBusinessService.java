package com.gogoyang.rpgapi.business.admin.common;

import com.gogoyang.rpgapi.meta.admin.entity.Admin;

public interface IAdminCommonBusinessService {
    Admin getAdminUserByToken(String token) throws Exception;
}

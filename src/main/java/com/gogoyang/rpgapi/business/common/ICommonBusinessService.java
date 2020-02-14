package com.gogoyang.rpgapi.business.common;

import com.gogoyang.rpgapi.meta.admin.entity.Admin;
import com.gogoyang.rpgapi.meta.user.entity.User;

import java.util.Map;

public interface ICommonBusinessService {
    User getUserByToken(String token) throws Exception;

    /**
     * 记录用户行为日志
     * @param in
     * @throws Exception
     */
    void createUserActionLog(Map in) throws Exception;

    Admin getAdminByToken(String token) throws Exception;
}

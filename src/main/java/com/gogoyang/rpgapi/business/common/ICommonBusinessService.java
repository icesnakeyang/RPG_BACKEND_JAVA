package com.gogoyang.rpgapi.business.common;

import com.gogoyang.rpgapi.meta.user.entity.User;

import java.util.Map;

public interface ICommonBusinessService {
    User getUserByToken(String token) throws Exception;

    void createUserActionLog(Map in) throws Exception;
}

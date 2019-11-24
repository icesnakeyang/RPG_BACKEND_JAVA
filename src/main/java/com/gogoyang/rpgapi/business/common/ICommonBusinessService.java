package com.gogoyang.rpgapi.business.common;

import com.gogoyang.rpgapi.meta.user.entity.User;

public interface ICommonBusinessService {
    User getUserByToken(String token) throws Exception;
}

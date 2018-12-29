package com.gogoyang.rpgapi.meta.user.service;

import com.gogoyang.rpgapi.meta.user.entity.User;

public interface IUserService {
    User getUserByUserId(Integer userId) throws Exception;
}

package com.gogoyang.rpgapi.meta.user.service;

import com.gogoyang.rpgapi.meta.user.entity.UserInfo;

import java.util.Map;

public interface IUserService {
    UserInfo getUserByUserId(String userId) throws Exception;

    void createUserInfo(UserInfo userInfo) throws Exception;

    UserInfo getUserByToken(String token) throws Exception;

    void updateUserInfo(UserInfo userInfo) throws Exception;

    UserInfo getUserByPhone(String phoneNumber) throws Exception;

    UserInfo getuserbyEmail(String emailAddress) throws Exception;
}

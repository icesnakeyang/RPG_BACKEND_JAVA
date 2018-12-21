package com.gogoyang.rpgapi.meta.user.userInfo.service;

import com.gogoyang.rpgapi.meta.user.userInfo.entity.UserInfo;

/**
 * IUserInfoService接口定义所有与UserInfo相关的操作
 * 凡需要调用UserInfoDao层的方法，都定义在这里。
 */
public interface IUserInfoService {

    UserInfo insertUser(UserInfo userInfo) throws Exception;

    UserInfo getUserByUsername(String username) throws Exception;

    UserInfo getUserByToken(String token) throws Exception;

    UserInfo getUserByUserId(Integer userId) throws Exception;

    void updateUser(UserInfo userInfo) throws Exception;

    String getUserName(Integer userId) throws Exception;

}

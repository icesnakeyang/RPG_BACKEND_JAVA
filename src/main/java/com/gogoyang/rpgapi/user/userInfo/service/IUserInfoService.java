package com.gogoyang.rpgapi.user.userInfo.service;

import com.gogoyang.rpgapi.user.userInfo.entity.UserInfo;

import java.util.ArrayList;

/**
 * IUserInfoService接口定义所有与UserInfo相关的操作
 * 凡需要调用UserInfoDao层的方法，都定义在这里。
 */
public interface IUserInfoService {

    UserInfo createUser(UserInfo userInfo) throws Exception;

    UserInfo loadUserByUsername(String username) throws Exception;

    UserInfo loadUserByToken(String token) throws Exception;

    UserInfo loadUserByUserId(Integer userId) throws Exception;

    void updateUser(UserInfo userInfo) throws Exception;

    void saveContactInfo(UserInfo userInfo) throws Exception;

    UserInfo checkToken(String token) throws Exception;

    String getUserName(Integer userId) throws Exception;

    void setDefaultPhone(UserInfo userInfo) throws Exception;

}

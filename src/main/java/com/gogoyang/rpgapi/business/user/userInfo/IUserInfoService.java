package com.gogoyang.rpgapi.business.user.userInfo;

public interface IUserInfoService {
    String getUserName(Integer userId) throws Exception;
    UserInfo getUserInfoByUserId(Integer userId) throws Exception;
}

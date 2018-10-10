package com.gogoyang.rpgapi.business.user.info.service;

import com.gogoyang.rpgapi.meta.user.userInfo.entity.UserInfo;

import java.util.Map;

public interface IUserInfoBusinessService {
    void saveUserContactInfo(Map in) throws Exception;
    void saveEmail(String strMail, UserInfo userInfo) throws Exception;
    void savePhone(String strPhone, UserInfo userInfo) throws Exception;
    void saveRealName(String strRealName, UserInfo userInfo) throws Exception;
}

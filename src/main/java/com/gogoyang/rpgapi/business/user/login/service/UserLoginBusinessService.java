package com.gogoyang.rpgapi.business.user.login.service;

import com.gogoyang.rpgapi.meta.user.userInfo.entity.UserInfo;
import com.gogoyang.rpgapi.meta.user.userInfo.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class UserLoginBusinessService implements IUserLoginBusinessService{
    private final IUserInfoService iUserInfoService;

    @Autowired
    public UserLoginBusinessService(IUserInfoService iUserInfoService) {
        this.iUserInfoService = iUserInfoService;
    }

    @Override
    public Map loginByEmail(Map in) throws Exception {
        String email=in.get("email").toString();
        String password=in.get("password").toString();

        UserInfo user=iUserInfoService.getUserByUsername()
        return null;
    }
}

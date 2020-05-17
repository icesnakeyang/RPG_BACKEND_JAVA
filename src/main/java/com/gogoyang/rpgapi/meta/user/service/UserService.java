package com.gogoyang.rpgapi.meta.user.service;

import com.gogoyang.rpgapi.meta.user.dao.UserDao;
import com.gogoyang.rpgapi.meta.user.entity.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService implements IUserService{
    private final UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public UserInfo getUserByUserId(String userId) throws Exception {
        Map qIn=new HashMap();
        qIn.put("userId", userId);
        UserInfo userInfo=userDao.getUserInfo(qIn);
        return userInfo;
    }

    @Override
    public void createUserInfo(UserInfo userInfo) throws Exception {
        userDao.createUserInfo(userInfo);
    }

    @Override
    public UserInfo getUserByToken(String token) throws Exception {
        Map qIn=new HashMap();
        qIn.put("token", token);
        UserInfo user=userDao.getUserInfo(qIn);
        return user;
    }

    @Override
    public void updateUserInfo(UserInfo userInfo) throws Exception {
        userDao.updateUserInfo(userInfo);
    }

    @Override
    public UserInfo getUserByPhone(String phone) throws Exception {
        Map qIn=new HashMap();
        qIn.put("phone", phone);
        UserInfo user=userDao.getUserInfo(qIn);
        return user;
    }
}

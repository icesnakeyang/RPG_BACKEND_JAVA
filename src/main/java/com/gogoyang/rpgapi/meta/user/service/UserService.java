package com.gogoyang.rpgapi.meta.user.service;

import com.gogoyang.rpgapi.meta.user.dao.UserDao;
import com.gogoyang.rpgapi.meta.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService implements IUserService{
    private final UserDao userDao;

    @Autowired
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User getUserByUserId(Integer userId) throws Exception {
        User user=userDao.findByUserId(userId);
        return user;
    }
}

package com.gogoyang.rpgapi.meta.user.service;

import com.gogoyang.rpgapi.meta.user.dao.UserDao;
import com.gogoyang.rpgapi.meta.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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

    @Override
    @Transactional(rollbackOn = Exception.class)
    public User insert(User user) throws Exception {
        if(user.getUserId()!=null){
            throw new Exception("10014");
        }
        user=userDao.save(user);
        return user;
    }

    @Override
    public User getUserByToken(String token) throws Exception {
        User user=userDao.findByToken(token);
        return user;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void update(User user) throws Exception {
        if(user.getUserId()==null){
            throw new Exception("10010");
        }
        userDao.save(user);
    }

    @Override
    public User getUserByPhone(String phone) throws Exception {
        User user=userDao.findByPhone(phone);
        return user;
    }
}

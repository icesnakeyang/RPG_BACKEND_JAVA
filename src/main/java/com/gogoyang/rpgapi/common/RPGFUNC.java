package com.gogoyang.rpgapi.common;

import com.gogoyang.rpgapi.user.dao.UserDao;
import com.gogoyang.rpgapi.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RPGFUNC implements IRPGFUNC{
    private final UserDao userDao;

    @Autowired
    public RPGFUNC(UserDao userDao) {
        this.userDao = userDao;
    }

    public boolean checkToken(String token) {
        if (token == null) {
            return false;
        }
        User user = userDao.findByToken(token);
        if (user == null) {
            return false;
        }

        return true;
    }
}

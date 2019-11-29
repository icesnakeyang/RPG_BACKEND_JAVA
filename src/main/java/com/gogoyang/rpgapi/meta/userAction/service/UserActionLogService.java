package com.gogoyang.rpgapi.meta.userAction.service;

import com.gogoyang.rpgapi.meta.userAction.dao.UserActionLogDao;
import com.gogoyang.rpgapi.meta.userAction.entity.UserActionLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserActionLogService implements IUserActionLogService{
    private final UserActionLogDao userActionLogDao;

    public UserActionLogService(UserActionLogDao userActionLogDao) {
        this.userActionLogDao = userActionLogDao;
    }

    @Override
    public void createUserActionLog(UserActionLog userActionLog) throws Exception {
        userActionLogDao.save(userActionLog);
    }
}

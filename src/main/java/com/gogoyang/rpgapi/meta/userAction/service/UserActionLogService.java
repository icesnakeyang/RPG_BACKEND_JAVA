package com.gogoyang.rpgapi.meta.userAction.service;

import com.gogoyang.rpgapi.meta.userAction.dao.UserActionLogDao;
import com.gogoyang.rpgapi.meta.userAction.entity.UserActionLog;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

@Service
public class UserActionLogService implements IUserActionLogService {
    private final UserActionLogDao userActionLogDao;

    public UserActionLogService(UserActionLogDao userActionLogDao) {
        this.userActionLogDao = userActionLogDao;
    }

    /**
     * 创建一个用户行为日志
     *
     * @param userActionLog
     * @throws Exception
     */
    @Override
    public void createUserActionLog(UserActionLog userActionLog) throws Exception {
        userActionLogDao.createUserActionLog(userActionLog);
    }

    /**
     * 查询用户行为记录
     *
     * @param qIn offset
     *            size
     * @return
     * @throws Exception
     */
    @Override
    public ArrayList<UserActionLog> listUserActionLog(Map qIn) throws Exception {
        ArrayList<UserActionLog> userActionLogs = userActionLogDao.listUserActionLog(qIn);
        return userActionLogs;
    }

    /**
     * 统计用户行为记录总数
     * @return
     * @throws Exception
     */
    @Override
    public Integer totalUserActionLog() throws Exception {
        Integer total = userActionLogDao.totalUserActionLog();
        return total;
    }
}

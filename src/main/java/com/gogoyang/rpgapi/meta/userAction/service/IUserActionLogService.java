package com.gogoyang.rpgapi.meta.userAction.service;

import com.gogoyang.rpgapi.meta.userAction.entity.UserActionLog;

import java.util.ArrayList;
import java.util.Map;

public interface IUserActionLogService {
    /**
     * 创建一个用户行为日志
     * @param userActionLog
     * @throws Exception
     */
    void createUserActionLog(UserActionLog userActionLog) throws Exception;

    /**
     * 查询用户行为记录
     * @param qIn
     * offset
     * size
     * @return
     */
    ArrayList<UserActionLog> listUserActionLog(Map qIn) throws Exception;

    /**
     *
     * @return
     * @throws Exception
     */
    Integer totalUserActionLog() throws Exception;
}

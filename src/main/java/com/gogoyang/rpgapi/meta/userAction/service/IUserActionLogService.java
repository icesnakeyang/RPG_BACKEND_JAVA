package com.gogoyang.rpgapi.meta.userAction.service;

import com.gogoyang.rpgapi.meta.userAction.entity.UserActionLog;

public interface IUserActionLogService {
    void createUserActionLog(UserActionLog userActionLog) throws Exception;
}

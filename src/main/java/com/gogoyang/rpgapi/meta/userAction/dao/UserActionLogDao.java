package com.gogoyang.rpgapi.meta.userAction.dao;

import com.gogoyang.rpgapi.meta.userAction.entity.UserActionLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.Map;

@Mapper
public interface UserActionLogDao{
    /**
     * 创建一个用户行为日志
     * @param userActionLog
     */
    void createUserActionLog(UserActionLog userActionLog);

    /**
     * 查询用户行为记录
     * @param qIn
     * offset
     * size
     * @return
     */
    ArrayList<UserActionLog> listUserActionLog(Map qIn);
}

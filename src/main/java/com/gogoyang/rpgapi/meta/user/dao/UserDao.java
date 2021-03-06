package com.gogoyang.rpgapi.meta.user.dao;

import com.gogoyang.rpgapi.meta.user.entity.UserInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.Map;

@Mapper
public interface UserDao {
    /**
     * create user
     * @param userInfo
     */
    void createUserInfo(UserInfo userInfo);

    /**
     * 读取用户信息
     * @param qIn
     * userId
     * phone
     * token
     * email
     * @return
     */
    UserInfo getUserInfo(Map qIn);

    /**
     * 修改用户信息
     * @param userInfo
     */
    void updateUserInfo(UserInfo userInfo);

    /**
     * 读取用户列表
     * @param qIn
     * phone
     * @return
     */
    ArrayList<UserInfo> listUserInfo(Map qIn);
}

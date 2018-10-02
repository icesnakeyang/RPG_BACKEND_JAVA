package com.gogoyang.rpgapi.user.userInfo.dao;

import com.gogoyang.rpgapi.user.userInfo.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoDao extends JpaRepository<UserInfo, Integer> {
    /**
     * 根据username查询
     *
     * @param username
     * @return
     */
    UserInfo findByUsername(String username);

    /**
     * 根据用户token查询
     *
     * @param token
     * @return
     */
    UserInfo findByToken(String token);

    /**
     * 根据用户Id查询
     *
     * @param id
     * @return
     */
    UserInfo findByUserId(Integer id);
}

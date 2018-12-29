package com.gogoyang.rpgapi.meta.user.userInfo.dao;

import com.gogoyang.rpgapi.meta.user.userInfo.entity.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoDao extends JpaRepository<UserInfo, Integer> {
    /**
     * 根据用户的email查询用户
     * @param email
     * @param password
     * @return
     */
    UserInfo findByEmailAndPassword(String email, String password);

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

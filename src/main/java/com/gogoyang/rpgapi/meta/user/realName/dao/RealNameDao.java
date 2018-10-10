package com.gogoyang.rpgapi.meta.user.realName.dao;

import com.gogoyang.rpgapi.meta.user.realName.entity.RealName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface RealNameDao extends JpaRepository<RealName,Integer> {
    /**
     * 读取用户所有曾经使用过的实名
     * @param userId
     * @return
     */
    ArrayList<RealName> findAllByUserId(Integer userId);

    /**
     * 读取用户当前正在使用的实名
     * @param userId
     * @return
     */
    RealName findByUserIdAndDisableTimeIsNull(Integer userId);
}

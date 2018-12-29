package com.gogoyang.rpgapi.meta.realname.dao;

import com.gogoyang.rpgapi.meta.realname.entity.RealName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RealNameDao extends JpaRepository<RealName, Integer> {
    RealName findByUserId(Integer userId);
}

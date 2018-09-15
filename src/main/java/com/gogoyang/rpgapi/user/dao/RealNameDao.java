package com.gogoyang.rpgapi.user.dao;

import com.gogoyang.rpgapi.user.entity.RealName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RealNameDao extends JpaRepository<RealName,Integer> {
    RealName findByUserId(Integer userId);
}

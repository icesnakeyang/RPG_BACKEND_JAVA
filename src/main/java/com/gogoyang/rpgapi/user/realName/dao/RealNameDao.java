package com.gogoyang.rpgapi.user.realName.dao;

import com.gogoyang.rpgapi.user.realName.entity.RealName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface RealNameDao extends JpaRepository<RealName,Integer> {
    ArrayList<RealName> findAllByUserId(Integer userId);
}

package com.gogoyang.rpgapi.topUp.dao;

import com.gogoyang.rpgapi.topUp.entity.TopUp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopUpDao extends JpaRepository<TopUp, Integer> {
}

package com.gogoyang.rpgapi.meta.topUp.dao;

import com.gogoyang.rpgapi.meta.topUp.entity.TopUp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TopUpDao extends JpaRepository<TopUp, Integer> {
}

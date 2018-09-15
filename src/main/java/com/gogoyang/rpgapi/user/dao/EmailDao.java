package com.gogoyang.rpgapi.user.dao;

import com.gogoyang.rpgapi.user.entity.Email;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailDao extends JpaRepository<Email, Integer> {
    Email findByUserId(Integer userId);

}

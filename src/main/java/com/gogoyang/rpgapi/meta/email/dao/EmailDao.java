package com.gogoyang.rpgapi.meta.email.dao;

import com.gogoyang.rpgapi.meta.email.entity.Email;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailDao extends JpaRepository<Email, Integer> {
    Email findByEmail(String email);
}

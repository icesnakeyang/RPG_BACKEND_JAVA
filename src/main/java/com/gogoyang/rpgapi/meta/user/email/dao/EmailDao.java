package com.gogoyang.rpgapi.meta.user.email.dao;

import com.gogoyang.rpgapi.meta.user.email.entity.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface EmailDao extends JpaRepository<Email, Integer> {
    ArrayList<Email> findByUserId(Integer userId);
    Email findByEmail(String email);
}

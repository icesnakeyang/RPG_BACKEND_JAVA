package com.gogoyang.rpgapi.meta.email.dao;

import com.gogoyang.rpgapi.meta.email.entity.Email;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface EmailDao extends JpaRepository<Email, Integer> {
    Email findByEmail(String email);
    ArrayList<Email> findAllByUserId(Integer userId);
    Email findByUserIdAndIsDefaultIsTrue(Integer userId);
}

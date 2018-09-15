package com.gogoyang.rpgapi.user.dao;

import com.gogoyang.rpgapi.user.entity.Phone;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhoneDao extends JpaRepository<Phone, Integer> {
    Phone findByUserId(Integer userId);
}

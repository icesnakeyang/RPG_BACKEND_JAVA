package com.gogoyang.rpgapi.meta.user.phone.dao;

import com.gogoyang.rpgapi.meta.user.phone.entity.Phone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface PhoneDao extends JpaRepository<Phone, Integer> {
    ArrayList<Phone> findAllByUserId(Integer userId);

    Phone findByPhone(String phone);
}

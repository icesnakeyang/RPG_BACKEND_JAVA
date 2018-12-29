package com.gogoyang.rpgapi.meta.phone.dao;

import com.gogoyang.rpgapi.meta.phone.entity.Phone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface PhoneDao extends JpaRepository<Phone, Integer> {
    Phone findByPhone(String phone);
    ArrayList<Phone> findAllByUserId(Integer userId);
    Phone findByUserIdAndIsDefaultIsTrue(Integer userId);
}

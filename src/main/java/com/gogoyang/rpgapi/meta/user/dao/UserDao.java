package com.gogoyang.rpgapi.meta.user.dao;

import com.gogoyang.rpgapi.meta.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User, Integer> {
    User findByUserId(Integer userId);

    User findByToken(String token);

    User findByPhone(String phone);
}

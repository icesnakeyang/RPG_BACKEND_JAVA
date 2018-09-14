package com.gogoyang.rpgapi.user.dao;


import com.gogoyang.rpgapi.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User, Integer> {
    User findByUsernameAndPassword(String username, String password);

    User findByToken(String token);

    User findByUserId(Integer id);
}

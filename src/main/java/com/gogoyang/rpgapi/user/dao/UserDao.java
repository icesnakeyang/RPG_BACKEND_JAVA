package com.gogoyang.rpgapi.user.dao;


import com.gogoyang.rpgapi.constant.RoleType;
import com.gogoyang.rpgapi.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDao extends JpaRepository<User, Integer> {
    User findByUsernameAndPassword(String username, String password);

    User findByToken(String token);

    User findByUserId(Integer id);

    Page<User> findByUserRole(RoleType roleType, Pageable pageable);

    Page<User> findByUserRoleNot(RoleType roleType, Pageable pageable);
}

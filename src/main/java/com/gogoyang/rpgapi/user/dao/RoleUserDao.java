package com.gogoyang.rpgapi.user.dao;

import com.gogoyang.rpgapi.user.entity.RoleUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleUserDao extends JpaRepository<RoleUser, Integer> {
    List<RoleUser> findByUserIdAndDisableIsNull(Integer userId);
}

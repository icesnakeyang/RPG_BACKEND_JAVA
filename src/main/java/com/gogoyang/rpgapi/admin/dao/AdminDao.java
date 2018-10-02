package com.gogoyang.rpgapi.admin.dao;

import com.gogoyang.rpgapi.admin.entity.Admin;
import com.gogoyang.rpgapi.constant.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface AdminDao extends JpaRepository<Admin, Integer> {
    Admin findByLoginName(String loginName);
    Admin findByToken(String token);

    ArrayList<Admin> findAllByRoleType(RoleType roleType);



}

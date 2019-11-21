package com.gogoyang.rpgapi.meta.admin.dao;

import com.gogoyang.rpgapi.meta.admin.entity.Admin;
import com.gogoyang.rpgapi.framework.constant.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface AdminDao extends JpaRepository<Admin, Integer> {
    Admin findByLoginName(String loginName);

    Admin findByToken(String token);

    Admin findAdminByPhone(String phone);

    ArrayList<Admin> findAllByRoleType(RoleType roleType);


}

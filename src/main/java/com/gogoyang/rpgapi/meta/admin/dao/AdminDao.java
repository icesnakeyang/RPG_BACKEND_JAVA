package com.gogoyang.rpgapi.meta.admin.dao;

import com.gogoyang.rpgapi.meta.admin.entity.Admin;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.Map;

@Mapper
public interface AdminDao{
    /**
     * 创建一个管理员用户
     * @param admin
     */
    void createAdmin(Admin admin);

    /**
     * 查询一个管理员
     * @param qIn
     * adminId
     * loginName
     * password
     * token
     * phone
     * @return
     */
    Admin getAdmin(Map qIn);

    /**
     * 查询多个管理员
     * @param qIn
     * roleType
     * @return
     */
    ArrayList<Admin> listAdmin(Map qIn);

    void updateAdmin(Admin admin);

    void deleteAdmin(String adminId);
}

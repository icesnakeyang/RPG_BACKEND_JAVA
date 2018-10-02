package com.gogoyang.rpgapi.admin.service;

import com.gogoyang.rpgapi.admin.entity.Admin;
import com.gogoyang.rpgapi.constant.RoleType;
import com.gogoyang.rpgapi.job.jobMatch.entity.JobMatch;
import com.gogoyang.rpgapi.user.userInfo.entity.UserInfo;

import java.util.ArrayList;

public interface IAdminService {
    Admin loadAdminByLoginName(String loginName) throws Exception;

    Admin createAdmin(Admin admin) throws Exception;

    Admin loadAdminByToken(String token) throws Exception;

    void matchJob(JobMatch jobMatch) throws Exception;

    ArrayList<Admin> loadAdminByRoleType(RoleType roleType) throws Exception;

    ArrayList loadRoleTypes(RoleType roleType) throws Exception;

    ArrayList<UserInfo> loadJobAppliedUser(Integer jobId) throws Exception;
}

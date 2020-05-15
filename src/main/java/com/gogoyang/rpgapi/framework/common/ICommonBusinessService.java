package com.gogoyang.rpgapi.framework.common;

import com.gogoyang.rpgapi.meta.admin.entity.Admin;
import com.gogoyang.rpgapi.meta.job.entity.Job;
import com.gogoyang.rpgapi.meta.user.entity.UserInfo;

import java.util.Map;

public interface ICommonBusinessService {
    UserInfo getUserByToken(String token) throws Exception;

    Job getJobTinyByJobId(String jobId) throws Exception;

    Job getJobDetailByJobId(String jobId) throws Exception;

    /**
     * 记录用户行为日志
     * @param in
     * @throws Exception
     */
    void createUserActionLog(Map in) throws Exception;

    Admin getAdminByToken(String token) throws Exception;

    /**
     * 计算并返回一个用户的账户统计信息
     * 更新用户userInfo表的账户汇总信息
     * @param userId
     * @return
     * @throws Exception
     */
    Map sumUserAccount(String userId) throws Exception;
}

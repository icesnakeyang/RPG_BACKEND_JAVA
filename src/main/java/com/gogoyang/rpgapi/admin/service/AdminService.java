package com.gogoyang.rpgapi.admin.service;

import com.gogoyang.rpgapi.admin.dao.AdminDao;
import com.gogoyang.rpgapi.admin.entity.Admin;
import com.gogoyang.rpgapi.constant.RoleType;
import com.gogoyang.rpgapi.job.meta.jobApply.entity.JobApply;
import com.gogoyang.rpgapi.job.meta.jobApply.service.IJobApplyService;
import com.gogoyang.rpgapi.job.meta.jobMatch.entity.JobMatch;
import com.gogoyang.rpgapi.job.meta.jobMatch.service.IJobMatchService;
import com.gogoyang.rpgapi.user.userInfo.entity.UserInfo;
import com.gogoyang.rpgapi.user.userInfo.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class AdminService implements IAdminService {
    private final AdminDao adminDao;
    private final IJobMatchService iJobMatchService;
    private final IJobApplyService iJobApplyService;
    private final IUserInfoService iUserInfoService;

    @Autowired
    public AdminService(AdminDao adminDao, IJobMatchService iJobMatchService,
                        IJobApplyService iJobApplyService,
                        IUserInfoService iUserInfoService) {
        this.adminDao = adminDao;
        this.iJobMatchService = iJobMatchService;
        this.iJobApplyService=iJobApplyService;
        this.iUserInfoService=iUserInfoService;
    }

    /**
     * 根据loginName读取Admin
     *
     * @param loginName
     * @return
     * @throws Exception
     */
    @Override
    public Admin loadAdminByLoginName(String loginName) throws Exception {
        /**
         * adminDao.findByLoginName
         */
        Admin admin = adminDao.findByLoginName(loginName);
        return admin;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Admin createAdmin(Admin admin) throws Exception {
        /**
         * adminDao.save(admin)
         */
        admin.setAdminId(adminDao.save(admin).getAdminId());
        return admin;
    }

    @Override
    public Admin loadAdminByToken(String token) throws Exception {
        Admin admin = adminDao.findByToken(token);
        return admin;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void matchJob(JobMatch jobMatch) throws Exception {
        /**
         * 创建一个jobMatch
         * 搜索jobApply，把申请任务的用户的申请日志处理为已分配，MATCHED
         */
        iJobMatchService.createJobMatch(jobMatch);
        JobApply jobApply=new JobApply();
        jobApply.setApplyUserId(jobMatch.getMatchUserId());
        jobApply.setJobId(jobMatch.getJobId());
        iJobApplyService.matchJobApply(jobApply);
    }

    /**
     * 根据用户类型读取所有管理员用户
     *
     * @param roleType
     * @return
     * @throws Exception
     */
    @Override
    public ArrayList<Admin> loadAdminByRoleType(RoleType roleType) throws Exception {
        ArrayList<Admin> admins = adminDao.findAllByRoleType(roleType);
        return admins;
    }

    /**
     * 读取所有RoleType里的枚举值
     * @return
     * @throws Exception
     */
    @Override
    public ArrayList loadRoleTypes(RoleType roleType) throws Exception {
        ArrayList roles=new ArrayList();

        for(RoleType s:RoleType.values()) {
            if(s.ordinal()>roleType.ordinal()) {
                Map map = new HashMap();
                map.put("value", s.ordinal());
                map.put("label", s);
                roles.add(map);
            }
        }
        return roles;
    }

    /**
     * 根据jobId，查找所有申请过该任务，且未被处理的用户。
     *
     * @param jobId
     * @return
     * @throws Exception
     */
    @Override
    public ArrayList<UserInfo> loadJobAppliedUser(Integer jobId) throws Exception {
        /**
         * 首先，根据JobId查找jobApplyLog，result==null
         * 然后，查询JobMatch里是否已经分配了
         * 根据jobApplyLog.applyUserId查找userInfo
         * 返回所有的UserInfo
         *
         */
        ArrayList<JobApply> jobApplies = iJobApplyService.loadJobApplyByJobId(jobId);
        ArrayList<UserInfo> users = new ArrayList<UserInfo>();
        for (int i = 0; i < jobApplies.size(); i++) {
            //检查是否已经分配
            JobMatch jobMatch=iJobMatchService.loadJobMatchByUserIdAndJobId(
                    jobApplies.get(i).getApplyUserId(),
                    jobApplies.get(i).getJobId());
            if(jobMatch==null) {
                UserInfo userInfo = iUserInfoService.loadUserByUserId(jobApplies.get(i).getApplyUserId());
                if (userInfo != null) {
                    users.add(userInfo);
                }
            }
        }

        return users;
    }
}

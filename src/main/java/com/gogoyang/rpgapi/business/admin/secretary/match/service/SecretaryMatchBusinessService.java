package com.gogoyang.rpgapi.business.admin.secretary.match.service;

import com.gogoyang.rpgapi.framework.constant.JobStatus;
import com.gogoyang.rpgapi.framework.constant.LogStatus;
import com.gogoyang.rpgapi.framework.constant.RoleType;
import com.gogoyang.rpgapi.meta.admin.entity.Admin;
import com.gogoyang.rpgapi.meta.admin.service.IAdminService;
import com.gogoyang.rpgapi.meta.apply.entity.JobApply;
import com.gogoyang.rpgapi.meta.apply.service.IJobApplyService;
import com.gogoyang.rpgapi.meta.job.entity.Job;
import com.gogoyang.rpgapi.meta.job.service.IJobService;
import com.gogoyang.rpgapi.meta.match.entity.JobMatch;
import com.gogoyang.rpgapi.meta.match.service.IJobMatchService;
import com.gogoyang.rpgapi.meta.user.userInfo.entity.UserInfo;
import com.gogoyang.rpgapi.meta.user.userInfo.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class SecretaryMatchBusinessService implements ISecretaryMatchBusinessService {
    private final IAdminService iAdminService;
    private final IJobService iJobService;
    private final IJobApplyService iJobApplyService;
    private final IUserInfoService iUserInfoService;
    private final IJobMatchService iJobMatchService;

    @Autowired
    public SecretaryMatchBusinessService(IAdminService iAdminService, IJobService iJobService, IJobApplyService iJobApplyService, IUserInfoService iUserInfoService, IJobMatchService iJobMatchService) {
        this.iAdminService = iAdminService;
        this.iJobService = iJobService;
        this.iJobApplyService = iJobApplyService;
        this.iUserInfoService = iUserInfoService;
        this.iJobMatchService = iJobMatchService;
    }

    /**
     * 读取用户已经申请，但还没有成交的任务
     *
     * @param in
     * @return
     * @throws Exception
     */
    @Override
    public Map loadJobToMatch(Map in) throws Exception {
        String token = in.get("token").toString();
        Integer pageIndex = (Integer) in.get("pageIndex");
        Integer pageSize = (Integer) in.get("pageSize");

        //check operator permission
        Admin admin = iAdminService.loadAdminByToken(token);
        if (admin == null) {
            throw new Exception("10004");
        }

        if (admin.getRoleType() != RoleType.SECRETARY) {
            throw new Exception("10034");
        }

        /**
         * 1、读取所有job，JobStatus.MATCHING
         * 2、逐条查询jobApply，如果有，添加到list
         */
        Page<Job> jobs = iJobService.loadJobByStatus(JobStatus.MATCHING, pageIndex, pageSize);

        ArrayList<Job> jobsOut = new ArrayList<Job>();

        for (int i = 0; i < jobs.getContent().size(); i++) {
            ArrayList<JobApply> jobApplies = iJobApplyService.loadJobApplyByJobId(
                    jobs.getContent().get(i).getJobId());
            if (jobApplies.size() > 0) {
                //加上用户名
                jobs.getContent().get(i).setPartyAName(iUserInfoService.getUserName(
                        jobs.getContent().get(i).getPartyAId()));
                jobsOut.add(jobs.getContent().get(i));
            }
        }
        Map out = new HashMap();
        out.put("jobs", jobsOut);
        return out;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void matchJobToUser(Map in) throws Exception {

        Integer jobId = (Integer) in.get("jobId");
        Integer userId = (Integer) in.get("userId");
        String token=in.get("token").toString();

        /**
         * only secretary can match job
         */
        Admin admin = iAdminService.loadAdminByToken(token);
        if (admin == null) {
            throw new Exception("10004");
        }
        if (admin.getRoleType() != RoleType.SECRETARY) {
            throw new Exception("10034");
        }

        /**
         * 首先，查询JobMatch, jobId, userId，processResult==null,
         * 如果有记录即说明，已经保存了该用户对该任务的分配
         * 如果没有记录，增加新的记录。
         *
         */
        JobMatch checkJobMatch = iJobMatchService.loadJobMatchByUserIdAndJobId(userId, jobId);
        if (checkJobMatch != null) {
            throw new Exception("10012");
        }

        /**
         * 首先，创建一个jobMatch
         * 然后，处理用户的任务申请
         * 1、createJobMatch
         * 2、loadJobApplyBy jobId and userId
         */


        JobMatch jobMatch = new JobMatch();
        jobMatch.setJobId(jobId);
        jobMatch.setMatchTime(new Date());
        jobMatch.setMatchUserId(userId);
        iJobMatchService.insertJobMatch(jobMatch);

        JobApply jobApply = iJobApplyService.loadJobApplyByUserIdAndJobId(userId, jobId);
        if (jobApply == null) {
            throw new Exception("10012");
        }
        jobApply.setProcessResult(LogStatus.MATCHED);
        jobApply.setProcessTime(new Date());
        jobApply.setProcessUserId(-1);
        iJobApplyService.updateJobApply(jobApply);
    }

    @Override
    public ArrayList<UserInfo> loadApplyUserByJobId(Integer jobId) throws Exception {
        return null;
    }
}

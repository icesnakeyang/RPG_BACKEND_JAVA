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
    public SecretaryMatchBusinessService(IAdminService iAdminService, IJobService iJobService,
                                         IJobApplyService iJobApplyService,
                                         IUserInfoService iUserInfoService,
                                         IJobMatchService iJobMatchService) {
        this.iAdminService = iAdminService;
        this.iJobService = iJobService;
        this.iJobApplyService = iJobApplyService;
        this.iUserInfoService = iUserInfoService;
        this.iJobMatchService = iJobMatchService;
    }

    /**
     * 读取所有有用户申请的任务，以便秘书处理。
     *
     * @param in
     * @return
     * @throws Exception
     */
    @Override
    public Map listJobToMatch(Map in) throws Exception {
        /**
         * 检查当前用户是否RPG秘书
         * 读取job, status==pending, matching， order by created_time desc
         * 循环，查询每个job的jobApply记录
         */
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
         * 1、读取所有job，JobStatus.MATCHING，和status.pending
         * 2、逐条查询jobApply，如果有，添加到list
         */
        Map qIn=new HashMap();
        qIn.put("type", "jobtomatch");
        qIn.put("pageIndex", pageIndex);
        qIn.put("pageSize", pageSize);
        Page<Job> jobs = iJobService.listJobByStausMap(qIn);

        ArrayList<Job> jobsOut = new ArrayList<Job>();

        for (int i = 0; i < jobs.getContent().size(); i++) {
            ArrayList<JobApply> jobApplies = iJobApplyService.listJobApplyByJobId(
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

    /**
     * 分配一个任务给一个用户
     *
     * @param in
     * @throws Exception
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public void matchJobToUser(Map in) throws Exception {

        Integer jobId = (Integer) in.get("jobId");
        Integer userId = (Integer) in.get("userId");
        String token = in.get("token").toString();

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
         * 首先，查询userId是否已经申请了jobId的任务，且未处理
         * 如果用户有申请，就直接处理jobApply，把任务直接给userId，修改job 为 process
         * 如果没有申请，则创建jobMatch，把任务匹配给userId，等待用户处理。
         */
        JobApply jobApply = iJobApplyService.loadJobApplyByUserIdAndJobId(userId, jobId);
        if (jobApply != null) {
            //处理jobApply为matched
            jobApply.setProcessResult(LogStatus.MATCHED);
            jobApply.setProcessUserId(admin.getAdminId());
            jobApply.setProcessTime(new Date());
            iJobApplyService.updateJobApply(jobApply);

            //创建一个match日志，直接处理成功

            //把job status 改成process
        }

        jobApply.setProcessResult(LogStatus.MATCHED);
        jobApply.setProcessTime(new Date());
        jobApply.setProcessUserId(-1);
        iJobApplyService.updateJobApply(jobApply);


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

        jobApply = iJobApplyService.loadJobApplyByUserIdAndJobId(userId, jobId);
        if (jobApply == null) {
            throw new Exception("10012");
        }
        jobApply.setProcessResult(LogStatus.MATCHED);
        jobApply.setProcessTime(new Date());
        jobApply.setProcessUserId(-1);
        iJobApplyService.updateJobApply(jobApply);
    }

    @Override
    public Map listUserAppliedJob(Map in) throws Exception {
        /**
         * 1、检查用户权限
         * 2、读取jobApply，result=null
         */
        String token=in.get("token").toString();
        Integer pageIndex=(Integer)in.get("pageIndex");
        Integer pageSize=(Integer)in.get("pageSize");

        UserInfo userInfo=iUserInfoService.getUserByToken(token);
        if(userInfo==null){
            throw new Exception("10004");
        }

        Map qIn=new HashMap();
        qIn.put("pageIndex", pageIndex);
        qIn.put("pageSize", pageSize);
        Page<JobApply> jobApplies=iJobApplyService.listJobApply(qIn);

        Map out=new HashMap();
        out.put("jobApplies", jobApplies);
        return out;
    }

    /**
     * list users history
     * @param in
     * @return
     * @throws Exception
     */
    @Override
    public Map listApplyHistory(Map in) throws Exception {
        String token=in.get("token").toString();
        Integer pageIndex=(Integer)in.get("pageIndex");
        Integer pageSize=(Integer)in.get("pageSize");
        Integer userId=(Integer)in.get("userId");

        Admin admin=iAdminService.loadAdminByToken(token);
        if(admin==null){
            throw new Exception("10004");
        }

        Page<JobApply> jobApplies=iJobApplyService.listJobapplybyUserId(userId, pageIndex, pageSize);

        Map out=new HashMap();
        out.put("applyList", jobApplies);
        return out;
    }

    /**
     * list new applies
     * @param in
     * @return
     * @throws Exception
     */
    @Override
    public Map listJobMatching(Map in) throws Exception {
        /**
         * list job status=matching
         */
        String token=in.get("token").toString();
        Integer pageIndex=(Integer)in.get("pageIndex");
        Integer pageSize=(Integer)in.get("pageSize");

        Admin admin=iAdminService.loadAdminByToken(token);
        if(admin==null){
            throw new Exception("10004");
        }

        Page<Job> jobs=iJobService.listJobByStatus(JobStatus.MATCHING, pageIndex, pageSize);

        Map out=new HashMap();
        out.put("newApplyList", jobs);
        return out;
    }

    @Override
    public Map listApplyByJobId(Map in) throws Exception {
        String token = in.get("token").toString();
        Integer jobId = (Integer) in.get("jobId");
        Integer pageIndex=(Integer)in.get("pageIndex");
        Integer pageSize=(Integer)in.get("pageSize");

        /**
         * 当前操作用户必须是RPG秘书权限
         */
        Admin admin = iAdminService.loadAdminByToken(token);
        if (admin == null) {
            throw new Exception("10004");
        }
        if (admin.getRoleType() != RoleType.SECRETARY) {
            throw new Exception("10040");
        }

        /**
         * read jobApply by jobId where processResult==null
         */
        ArrayList<JobApply> jobApplies = iJobApplyService.listJobApplyByJobId(jobId);


        Map out = new HashMap();
        out.put("users", jobApplies);
        return out;
    }
}

package com.gogoyang.rpgapi.business.job.myJob.pending.service;

import com.gogoyang.rpgapi.framework.common.ICommonBusinessService;
import com.gogoyang.rpgapi.framework.constant.JobStatus;
import com.gogoyang.rpgapi.framework.constant.LogStatus;
import com.gogoyang.rpgapi.meta.apply.entity.JobApply;
import com.gogoyang.rpgapi.meta.apply.service.IJobApplyService;
import com.gogoyang.rpgapi.meta.job.entity.Job;
import com.gogoyang.rpgapi.meta.job.service.IJobService;
import com.gogoyang.rpgapi.meta.user.entity.UserInfo;
import com.gogoyang.rpgapi.meta.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class MyPendingBusinessService implements IMyPendingBusinessService {
    private final IJobService iJobService;
    private final IUserService iUserService;
    private final IJobApplyService iJobApplyService;
    private final ICommonBusinessService iCommonBusinessService;

    @Autowired
    public MyPendingBusinessService(IJobService iJobService,
                                    IUserService iUserService,
                                    IJobApplyService iJobApplyService,
                                    ICommonBusinessService iCommonBusinessService) {
        this.iJobService = iJobService;
        this.iUserService = iUserService;
        this.iJobApplyService = iJobApplyService;
        this.iCommonBusinessService = iCommonBusinessService;
    }

    @Override
    public Map listMyPendingJob(Map in) throws Exception {
        String token = in.get("token").toString();
        UserInfo user = iUserService.getUserByToken(token);
        String partyAId = user.getUserId();
        Integer pageIndex = (Integer) in.get("pageIndex");
        Integer pageSize = (Integer) in.get("pageSize");
        ArrayList<Job> jobs = iJobService.listMyPendingJob(partyAId, pageIndex, pageSize);
        Map out = new HashMap();
        out.put("jobs", jobs);
        return out;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateJob(Map in) throws Exception {
        /**
         * 首先检查用户
         * 读取job
         * 检查job状态是否为pending
         * 检查用户是否为partyA
         * 修改job和jobDetail
         */
        String token = in.get("token").toString();
        String jobId = (String) in.get("jobId");
        String title = in.get("title").toString();
        String code = in.get("code").toString();
        Integer days = (Integer) in.get("days");
        Double price = (Double) in.get("price");
        String jobDetail = in.get("jobDetail").toString();

        UserInfo user = iUserService.getUserByToken(token);

        if (user == null) {
            throw new Exception("10004");
        }

        Job job = iJobService.getJobTinyByJobId(jobId);

        if (job == null) {
            throw new Exception("10100");
        }

        if (!job.getStatus().equals(JobStatus.PENDING)) {
            throw new Exception("10101");
        }

        if (!user.getUserId().equals(job.getPartyAId())) {
            throw new Exception("10102");
        }

        job.setTitle(title);
        job.setCode(code);
        job.setDays(days);
        job.setPrice(price);
        job.setDetail(jobDetail);
        iJobService.updateJob(job);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePendingJob(Map in) throws Exception {
        String token = in.get("token").toString();
        String jobId = in.get("jobId").toString();

        //read current user
        UserInfo user = iCommonBusinessService.getUserByToken(token);

        //read the job
        Job job = iJobService.getJobTinyByJobId(jobId);
        if (job == null) {
            //no such job
            throw new Exception("30001");
        }
        //check the job status must be PENDING or MATCHING
        if (!job.getStatus().equals(JobStatus.PENDING) &&
                !job.getStatus().equals(JobStatus.MATCHING)) {
            throw new Exception("30005");
        }

        /**
         * 检查是否有未处理的乙方申请
         */
        Map qIn = new HashMap();
        qIn.put("jobId", jobId);
        qIn.put("status", LogStatus.PENDING.toString());
        ArrayList<JobApply> jobApplies = iJobApplyService.listJobApply(qIn);
        if (jobApplies.size() > 0) {
            //存在未处理的用户申请，不能删除任务
            throw new Exception("10104");
        }
        //check the party a of this job must be current user
        if (!job.getPartyAId().equals(user.getUserId())) {
            throw new Exception("10105");
        }
        //delete
        iJobService.deleteJob(jobId);
    }

    @Override
    public Map getMyPendingJob(Map in) throws Exception {
        String token = in.get("token").toString();
        String jobId =  in.get("jobId").toString();

        UserInfo user = iCommonBusinessService.getUserByToken(token);

        Job job = iJobService.getJobDetailByJobId(jobId);

        job.setJobApplyNum(iJobApplyService.countApplyUsers(job.getJobId()));

        Map out = new HashMap();
        out.put("job", job);
        return out;
    }
}

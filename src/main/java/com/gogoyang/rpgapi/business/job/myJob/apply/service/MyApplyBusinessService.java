package com.gogoyang.rpgapi.business.job.myJob.apply.service;

import com.gogoyang.rpgapi.framework.common.ICommonBusinessService;
import com.gogoyang.rpgapi.framework.constant.JobStatus;
import com.gogoyang.rpgapi.framework.constant.LogStatus;
import com.gogoyang.rpgapi.meta.apply.entity.JobApply;
import com.gogoyang.rpgapi.meta.apply.service.IJobApplyService;
import com.gogoyang.rpgapi.meta.job.entity.Job;
import com.gogoyang.rpgapi.meta.job.service.IJobService;
import com.gogoyang.rpgapi.meta.realname.service.IRealNameService;
import com.gogoyang.rpgapi.meta.user.entity.UserInfo;
import com.gogoyang.rpgapi.meta.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class MyApplyBusinessService implements IMyApplyBusinessService {
    private final IJobApplyService iJobApplyService;
    private final IJobService iJobService;
    private final IUserService iUserService;
    private final ICommonBusinessService iCommonBusinessService;

    @Autowired
    public MyApplyBusinessService(IJobApplyService iJobApplyService,
                                  IJobService iJobService,
                                  IUserService iUserService,
                                  ICommonBusinessService iCommonBusinessService) {
        this.iJobApplyService = iJobApplyService;
        this.iJobService = iJobService;
        this.iUserService = iUserService;
        this.iCommonBusinessService = iCommonBusinessService;
    }

    /**
     * 读取所有我申请的，还未处理的任务。
     *
     * @param in
     * @return
     * @throws Exception
     */
    @Override
    public Map listJobByMyApply(Map in) throws Exception {
        /**
         * 参数：token
         * 1. 根据token读取userInfo，获得用户的userId
         * 2. 根据userId读取jobApply，获取用户的申请记录
         * 3. 根据jobId读取job信息
         */
        String token = in.get("token").toString();
        Integer pageIndex = (Integer) in.get("pageIndex");
        Integer pageSize = (Integer) in.get("pageSize");
        UserInfo user = iUserService.getUserByToken(token);
        if (user == null) {
            throw new Exception("10004");
        }
        Map qIn=new HashMap();
        qIn.put("applyUserId", user.getUserId());
        Integer offset =(pageIndex-1)*pageSize;
        qIn.put("offset", offset);
        qIn.put("size", pageSize);
        ArrayList<JobApply> myApplyList =iJobApplyService.listJobApply(qIn);
        ArrayList jobList = new ArrayList();
        for (int i = 0; i < myApplyList.size(); i++) {
            Job job = iJobService.getJobTinyByJobId(myApplyList.get(i).getJobId());
            if (job != null) {
                Integer applyNum = iJobApplyService.countApplyUsers(job.getJobId());
                Map theMap = new HashMap();
                theMap.put("job", job);
                theMap.put("apply", myApplyList.get(i));
                theMap.put("applyNum", applyNum);

                jobList.add(theMap);
            }
        }
        Map out = new HashMap();
        out.put("jobList", jobList);
        return out;
    }

    /**
     * 用户申请一个任务
     *
     * @param in
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void applyJob(Map in) throws Exception {
        //check token
        String token = in.get("token").toString();
        String jobId = (String) in.get("jobId");
        String content = (String) in.get("content");

        UserInfo user = iUserService.getUserByToken(token);
        if (user == null) {
            throw new Exception("10004");
        }

        //check common
        Job job = iJobService.getJobTinyByJobId(jobId);
        if (job == null) {
            throw new Exception("10005");
        }

        /**
         * 检查任务状态，只有PENGIND和MATCHING状态的任务才能申请
         */
        if (!job.getStatus().equals(JobStatus.MATCHING) &&
                !job.getStatus().equals(JobStatus.PENDING)) {
            throw new Exception("10006");
        }

        //检查当前用户是否为甲方
        if (job.getPartyAId().equals(user.getUserId())) {
            throw new Exception("10037");
        }

        //检查用户是否已经申请过该任务了
        Map qIn = new HashMap();
        qIn.put("jobId", jobId);
        qIn.put("status", LogStatus.PENDING);
        qIn.put("applyUserId", user.getUserId());
        ArrayList<JobApply> jobApplies = iJobApplyService.listJobApply(qIn);
        if (jobApplies.size() > 0) {
            //the common has applied by current user already
            throw new Exception("10007");
        }

        //保存任务申请日志
        JobApply jobApply = new JobApply();
        jobApply.setApplyTime(new Date());
        jobApply.setApplyUserId(user.getUserId());
        jobApply.setJobId(jobId);
        jobApply.setContent(content);
        jobApply.setStatus(LogStatus.PENDING.toString());
        iJobApplyService.insertJobApply(jobApply);

        //刷新job的applyNum次数，且把jobStatus改成Matching
        Job job2 = new Job();
        job2.setJobId(job.getJobId());
        Integer applyNum = iJobApplyService.countApplyUsers(jobId);
        job2.setJobApplyNum(applyNum);
        job2.setStatus(JobStatus.MATCHING.toString());
        iJobService.updateJob(job);
    }

    @Override
    public Map getMyApplyJob(Map in) throws Exception {
        String token = in.get("token").toString();
        String jobId = in.get("jobId").toString();

        UserInfo user = iCommonBusinessService.getUserByToken(token);

        Job job = iJobService.getJobTinyByJobId(jobId);

        Map out = new HashMap();
        out.put("job", job);

        return out;
    }
}

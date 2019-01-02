package com.gogoyang.rpgapi.business.job.myJob.apply.service;

import com.gogoyang.rpgapi.framework.constant.JobStatus;
import com.gogoyang.rpgapi.meta.apply.entity.JobApply;
import com.gogoyang.rpgapi.meta.apply.service.IJobApplyService;
import com.gogoyang.rpgapi.meta.job.entity.Job;
import com.gogoyang.rpgapi.meta.job.service.IJobService;
import com.gogoyang.rpgapi.meta.match.entity.JobMatch;
import com.gogoyang.rpgapi.meta.match.service.IJobMatchService;
import com.gogoyang.rpgapi.meta.realname.service.IRealNameService;
import com.gogoyang.rpgapi.meta.user.entity.User;
import com.gogoyang.rpgapi.meta.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class MyApplyBusinessService implements IMyApplyBusinessService{
    private final IJobApplyService iJobApplyService;
    private final IJobService iJobService;
    private final IJobMatchService iJobMatchService;
    private final IUserService iUserService;
    private final IRealNameService iRealNameService;

    @Autowired
    public MyApplyBusinessService(IJobApplyService iJobApplyService,
                                  IJobService iJobService,
                                  IJobMatchService iJobMatchService,
                                  IUserService iUserService,
                                  IRealNameService iRealNameService) {
        this.iJobApplyService = iJobApplyService;
        this.iJobService = iJobService;
        this.iJobMatchService = iJobMatchService;
        this.iUserService = iUserService;
        this.iRealNameService = iRealNameService;
    }

    /**
     * 读取所有我申请的，还未处理的任务。
     *
     * @param in
     * @return
     * @throws Exception
     */
    @Override
    public Map loadJobByMyApply(Map in) throws Exception {
        /**
         * 参数：token
         * 1. 根据token读取userInfo，获得用户的userId
         * 2. 根据userId读取jobApply，获取用户的申请记录
         * 3. 根据jobId读取job信息
         */
        String token=in.get("token").toString();
        User user=iUserService.getUserByToken(token);
        if(user==null){
            throw new Exception("10004");
        }
        ArrayList<JobApply> myApplyList = iJobApplyService.loadMyApplies(user.getUserId());
        ArrayList jobList = new ArrayList();
        for (int i = 0; i < myApplyList.size(); i++) {
            Job job = iJobService.getJobByJobId(myApplyList.get(i).getJobId());
            if (job != null) {
                job.setPartyAName(iRealNameService.getRealNameByUserId(job.getPartyAId()).getRealName());
                Integer applyNum = iJobApplyService.countApplyUsers(job.getJobId());
                Integer matchNum = iJobMatchService.countMatchingUsers(job.getJobId());
                Map theMap = new HashMap();
                theMap.put("job", job);
                theMap.put("apply", myApplyList.get(i));
                theMap.put("applyNum", applyNum);
                theMap.put("matchNum", matchNum);

                jobList.add(theMap);
            }
        }
        Map out=new HashMap();
        out.put("jobList", jobList);
        return out;
    }

    /**
     * 用户申请一个任务
     * @param in
     * @throws Exception
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public void applyJob(Map in) throws Exception {
        //check token
        String token = in.get("token").toString();
        Integer jobId=(Integer)in.get("jobId");
        String content=(String)in.get("content");

        User user=iUserService.getUserByToken(token);
        if(user==null){
            throw new Exception("10004");
        }

        //check common
        Job job = iJobService.getJobByJobId(jobId);
        if (job == null) {
            throw new Exception("10005");
        }

        //是否已成交，即不是Matching，也不是Pending
        if (job.getStatus().ordinal()!=JobStatus.MATCHING.ordinal() &&
                job.getStatus().ordinal()!=JobStatus.PENDING.ordinal()) {
            throw new Exception("10006");
        }

        //检查用户是否为甲方
        if(job.getPartyAId()==user.getUserId()){
            throw new Exception("10037");
        }

        //检查用户是否已经申请过该任务了
        JobApply jobApply=iJobApplyService.loadJobApplyByUserIdAndJobId(user.getUserId(), jobId);
        if(jobApply!=null){
            //the common has applied by current user already
            throw new Exception("10007");
        }

        //检查用户是否已经分配了该任务
        JobMatch jobMatch=iJobMatchService.loadJobMatchByUserIdAndJobId(user.getUserId(),jobId);
        if(jobMatch!=null){
            // the common has been assigned to this user already.
            throw new Exception("10049");
        }

        //保存任务申请日志
        jobApply = new JobApply();
        jobApply.setApplyTime(new Date());
        jobApply.setApplyUserId(user.getUserId());
        jobApply.setJobId(jobId);
        jobApply.setContent(content);
        iJobApplyService.insertJobApply(jobApply);

        //刷新job的applyNum次数，且把jobStatus改成Matching
        Integer applyNum=iJobApplyService.countApplyUsers(jobId);
        if(job.getJobApplyNum()!=applyNum){
            job.setJobApplyNum(applyNum);
        }
        if(job.getStatus()!=JobStatus.MATCHING){
            job.setStatus(JobStatus.MATCHING);
        }
        iJobService.updateJob(job);
    }
}

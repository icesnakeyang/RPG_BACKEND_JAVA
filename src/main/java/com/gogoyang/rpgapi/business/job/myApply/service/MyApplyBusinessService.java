package com.gogoyang.rpgapi.business.job.myApply.service;

import com.gogoyang.rpgapi.framework.constant.JobStatus;
import com.gogoyang.rpgapi.meta.apply.entity.JobApply;
import com.gogoyang.rpgapi.meta.apply.service.IJobApplyService;
import com.gogoyang.rpgapi.meta.job.entity.Job;
import com.gogoyang.rpgapi.meta.job.service.IJobService;
import com.gogoyang.rpgapi.meta.match.service.IJobMatchService;
import com.gogoyang.rpgapi.meta.user.userInfo.entity.UserInfo;
import com.gogoyang.rpgapi.meta.user.userInfo.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class MyApplyBusinessService implements IMyApplyBusinessService{
    private final IUserInfoService iUserInfoService;
    private final IJobApplyService iJobApplyService;
    private final IJobService iJobService;
    private final IJobMatchService iJobMatchService;

    @Autowired
    public MyApplyBusinessService(IUserInfoService iUserInfoService, IJobApplyService iJobApplyService,
                                  IJobService iJobService, IJobMatchService iJobMatchService) {
        this.iUserInfoService = iUserInfoService;
        this.iJobApplyService = iJobApplyService;
        this.iJobService = iJobService;
        this.iJobMatchService = iJobMatchService;
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
        UserInfo userInfo=iUserInfoService.loadUserByToken(token);
        if(userInfo==null){
            throw new Exception("10004");
        }
        ArrayList<JobApply> myApplyList = iJobApplyService.loadMyApplies(userInfo.getUserId());
        ArrayList jobList = new ArrayList();
        for (int i = 0; i < myApplyList.size(); i++) {
            Job job = iJobService.loadJobByJobId(myApplyList.get(i).getJobId());
            if (job != null) {
                job.setPartyAName(iUserInfoService.getUserName(job.getPartyAId()));
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

        UserInfo userInfo=iUserInfoService.loadUserByToken(token);
        if(userInfo==null){
            throw new Exception("10004");
        }

        //check job
        Job job = iJobService.loadJobByJobId(jobId);
        if (job == null) {
            throw new Exception("10005");
        }

        //是否已成交
        if (job.getStatus().ordinal()!=JobStatus.MATCHING.ordinal()) {
            throw new Exception("10006");
        }

        //检查用户是否为甲方
        if(job.getPartyAId()==userInfo.getUserId()){
            throw new Exception("10037");
        }

        //检查用户是否已经申请过该任务了
        JobApply jobApply=iJobApplyService.loadJobApplyByUserIdAndJobId(userInfo.getUserId(), jobId);
        if(jobApply!=null){
            //the job has applied by current user already
            throw new Exception("10007");
        }

        //保存任务申请日志
        jobApply = new JobApply();
        jobApply.setApplyTime(new Date());
        jobApply.setApplyUserId(userInfo.getUserId());
        jobApply.setJobId(jobId);
        iJobApplyService.insertJobApply(jobApply);

        //刷新job的applyNum次数
        Integer applyNum=iJobApplyService.countApplyUsers(jobId);
        if(job.getJobApplyNum()!=applyNum){
            job.setJobApplyNum(applyNum);
            iJobService.updateJob(job);
        }
    }
}

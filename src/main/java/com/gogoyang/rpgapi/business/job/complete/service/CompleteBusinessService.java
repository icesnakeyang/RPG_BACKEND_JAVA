package com.gogoyang.rpgapi.business.job.complete.service;

import com.gogoyang.rpgapi.business.user.userInfo.UserInfo;
import com.gogoyang.rpgapi.framework.constant.JobStatus;
import com.gogoyang.rpgapi.framework.constant.LogStatus;
import com.gogoyang.rpgapi.meta.complete.entity.JobComplete;
import com.gogoyang.rpgapi.meta.complete.service.IJobCompleteService;
import com.gogoyang.rpgapi.meta.job.entity.Job;
import com.gogoyang.rpgapi.meta.job.service.IJobService;
import com.gogoyang.rpgapi.meta.user.entity.User;
import com.gogoyang.rpgapi.meta.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

@Service
public class CompleteBusinessService implements ICompleteBusinessService {
    private final IJobCompleteService iJobCompleteService;
    private final IJobService iJobService;
    private final IUserService iUserService;

    @Autowired
    public CompleteBusinessService(IJobCompleteService iJobCompleteService,
                                   IJobService iJobService, IUserService iUserService) {
        this.iJobCompleteService = iJobCompleteService;
        this.iJobService = iJobService;
        this.iUserService = iUserService;
    }

    /**
     * 创建一个新的验收任务
     *
     * @param in
     * @throws Exception
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public void createJobComplete(Map in) throws Exception {
        String token = in.get("token").toString();
        Integer jobId = (Integer) in.get("jobId");
        String content = in.get("content").toString();

        User user = iUserService.getUserByToken(token);
        if (user == null) {
            throw new Exception("10004");
        }

        JobComplete jobComplete = new JobComplete();
        jobComplete.setContent(content);
        jobComplete.setCreatedTime(new Date());
        jobComplete.setCreatedUserId(user.getUserId());
        jobComplete.setJobId(jobId);
        iJobCompleteService.insertJobComplete(jobComplete);
    }

    /**
     * 获取任务的所有验收日志
     *
     * @param in
     * @return
     * @throws Exception
     */
    @Override
    public Page<JobComplete> loadCompleteList(Map in) throws Exception {
        Integer jobId = (Integer) in.get("jobId");
        Integer pageIndex = (Integer) in.get("pageIndex");
        Integer pageSize = (Integer) in.get("pageSize");
        Page<JobComplete> completes = iJobCompleteService.loadJobCompleteByJobId(jobId, pageIndex, pageSize);
        return completes;
    }

    /**
     * 把我未读的验收任务日志设置为当前阅读
     *
     * @param in
     * @throws Exception
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public void setCompleteReadTime(Map in) throws Exception {
        Integer jobId = (Integer) in.get("jobId");
        String token = in.get("token").toString();
        User user = iUserService.getUserByToken(token);
        if (user == null) {
            throw new Exception("10004");
        }
        ArrayList<JobComplete> jobCompletes = iJobCompleteService.loadMyUnReadComplete(jobId, user.getUserId());

        for (int i = 0; i < jobCompletes.size(); i++) {
            jobCompletes.get(i).setReadTime(new Date());
            iJobCompleteService.updateJobComplete(jobCompletes.get(i));
        }
    }

    /**
     * 拒绝验收
     * 读取所有未处理验收申请
     * 设置成reject
     * @param in
     * @throws Exception
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public void rejectComplete(Map in) throws Exception {
        Integer jobId=(Integer)in.get("jobId");
        Job job=iJobService.getJobByJobIdTiny(jobId);
        if(job.getStatus()!=JobStatus.PROGRESS){
            throw new Exception("10063");
        }
        String token=in.get("token").toString();
        User user=iUserService.getUserByToken(token);
        String processRemark=null;
        if(in.get("processRemark")!=null){
            processRemark=in.get("processRemark").toString();
        }
        ArrayList<JobComplete> jobCompletes=iJobCompleteService.loadUnprocessComplete(jobId);
        for(int i=0;i<jobCompletes.size();i++){
            jobCompletes.get(i).setResult(LogStatus.REJECT);
            jobCompletes.get(i).setProcessRemark(processRemark);
            jobCompletes.get(i).setProcessTime(new Date());
            jobCompletes.get(i).setProcessUserId(user.getUserId());
            iJobCompleteService.updateJobComplete(jobCompletes.get(i));
        }
    }

    /**
     * 通过任务验收
     * @param in
     * @throws Exception
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public void acceptComplete(Map in) throws Exception {
        /**
         * 1、查询所有未处理的Complete，修改结果为Accept
         * 2、把job的Status修改为Accept
         * 3、给甲方增加honor
         * 4、给乙方增加honor
         * 5、刷新甲方和乙方的userinfo的honor值
         */
        //首先判断任务是否已经验收
        Integer jobId=(Integer)in.get("jobId");
        Job job=iJobService.getJobByJobIdTiny(jobId);
        if(job.getStatus()!= JobStatus.PROGRESS){
            throw new Exception("10063");
        }

        String token=in.get("token").toString();
        User user=iUserService.getUserByToken(token);
        String processRemark="";
        if(in.get("processRemark")!=null){
            processRemark=in.get("processRemark").toString();
        }
        ArrayList<JobComplete> jobCompletes=iJobCompleteService.loadUnprocessComplete(jobId);
        if(jobCompletes.size()==0){
            JobComplete jobComplete=new JobComplete();
            jobComplete.setProcessUserId(user.getUserId());
            jobComplete.setProcessTime(new Date());
            jobComplete.setProcessRemark(processRemark);
            jobComplete.setResult(LogStatus.ACCEPT);
            jobComplete.setCreatedTime(new Date());
            jobComplete.setCreatedUserId(user.getUserId());
            jobComplete.setJobId(jobId);
            jobComplete.setReadTime(new Date());
            iJobCompleteService.insertJobComplete(jobComplete);
        }else {
            for(int i=0;i<jobCompletes.size();i++){
                jobCompletes.get(i).setResult(LogStatus.ACCEPT);
                jobCompletes.get(i).setProcessRemark(processRemark);
                jobCompletes.get(i).setProcessTime(new Date());
                jobCompletes.get(i).setProcessUserId(user.getUserId());
                iJobCompleteService.updateJobComplete(jobCompletes.get(i));
            }
        }

        //把job设置为accept
        job.setStatus(JobStatus.ACCEPTANCE);
        iJobService.updateJob(job);

        //给甲方增加honor
        User userA=iUserService.getUserByUserId(job.getPartyAId());
        Double ha=0.0;
        if(userA()!=null){
            ha=userA.getHonor();
        }
        ha+=job.getPrice();
        userA.setHonor(ha);
        userA.setHonorIn(ha);
        iUserInfoService.updateUser(userA);

        //给乙方增加honor
        UserInfo userB=iUserInfoService.getUserByUserId(job.getPartyBId());
        Double hb=0.0;
        if(userB.getHonor()!=null){
            hb=userB.getHonor();
        }
        hb+=job.getPrice();
        userB.setHonor(hb);
        userB.setHonorIn(hb);
        iUserInfoService.updateUser(userB);

    }

    @Override
    public Integer countUnreadComplete(Map in) throws Exception {
        Integer userId;
        if(in.get("userId")==null){
            String token=in.get("token").toString();
            UserInfo userInfo = iUserInfoService.getUserByToken(token);
            userId=userInfo.getUserId();
        }else {
            userId=(Integer)in.get("userId");
        }
        Integer jobId=(Integer)in.get("jobId");
        ArrayList<JobComplete> jobCompletes=iJobCompleteService.loadMyUnReadComplete(jobId, userId);
        return jobCompletes.size();
    }
}

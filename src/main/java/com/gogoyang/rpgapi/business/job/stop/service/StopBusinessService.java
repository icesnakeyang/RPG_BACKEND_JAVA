package com.gogoyang.rpgapi.business.job.stop.service;

import com.gogoyang.rpgapi.framework.constant.JobStatus;
import com.gogoyang.rpgapi.framework.constant.LogStatus;
import com.gogoyang.rpgapi.meta.job.entity.Job;
import com.gogoyang.rpgapi.meta.job.service.IJobService;
import com.gogoyang.rpgapi.meta.stop.entity.JobStop;
import com.gogoyang.rpgapi.meta.stop.service.IJobStopService;
import com.gogoyang.rpgapi.meta.user.userInfo.entity.UserInfo;
import com.gogoyang.rpgapi.meta.user.userInfo.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

@Service
public class StopBusinessService implements IStopBusinessService {
    private final IUserInfoService iUserInfoService;
    private final IJobStopService iJobStopService;
    private final IJobService iJobService;

    @Autowired
    public StopBusinessService(IUserInfoService iUserInfoService, IJobStopService iJobStopService, IJobService iJobService) {
        this.iUserInfoService = iUserInfoService;
        this.iJobStopService = iJobStopService;
        this.iJobService = iJobService;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void createJobStop(Map in) throws Exception {
        /**
         * 甲乙双方同时只能处理一件终止申请，只要有终止未处理，就不能再次创建终止申请
         */
        Integer jobId = (Integer) in.get("jobId");
        JobStop jobStop = iJobStopService.loadJobStopUnProcess(jobId);
        if (jobStop != null) {
            throw new Exception("10069");
        }

        String token = in.get("token").toString();
        UserInfo userInfo = iUserInfoService.loadUserByToken(token);
        if (userInfo == null) {
            throw new Exception("10004");
        }

        String content = in.get("content").toString();
        jobStop = new JobStop();
        jobStop.setContent(content);
        jobStop.setCreatedTime(new Date());
        jobStop.setCreatedUserId(userInfo.getUserId());
        jobStop.setJobId(jobId);
        iJobStopService.insertJobStop(jobStop);
    }

    @Override
    public Page<JobStop> loadStopList(Map in) throws Exception {
        Integer jobId = (Integer) in.get("jobId");
        Integer pageIndex = (Integer) in.get("pageIndex");
        Integer pageSize = (Integer) in.get("pageSize");
        Page<JobStop> stops = iJobStopService.loadJobStopByJobId(jobId, pageIndex, pageSize);
        return stops;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void setStopReadTime(Map in) throws Exception {
        Integer jobId = (Integer) in.get("jobId");
        String token = in.get("token").toString();
        UserInfo userInfo = iUserInfoService.loadUserByToken(token);
        if (userInfo == null) {
            throw new Exception("10004");
        }
        ArrayList<JobStop> stops = iJobStopService.loadMyUnReadStop(jobId, userInfo.getUserId());

        for (int i = 0; i < stops.size(); i++) {
            stops.get(i).setReadTime(new Date());
            iJobStopService.updateJobStop(stops.get(i));
        }
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void rejectStop(Map in) throws Exception {
        /**
         * 实际上，终止任务是在甲乙双方线上线下提前沟通多次后达成的一致，终止仅仅是对系统管理的一个操作，并没有太大的实际意义。
         * 如果拒绝终止，把本次终止申请处理了就行了，不用上纲上线。
         * 甲乙双方只能同时处理一件终止申请，不能没完没了，也就是说，只要双方还有一条终止申请没有被处理，就不能再次提出终止。
         */
        Integer jobId = (Integer) in.get("jobId");
        Job job = iJobService.loadJobByJobIdTiny(jobId);
        if (job.getStatus() != JobStatus.PROGRESS) {
            throw new Exception("10070");
        }

        String token = in.get("token").toString();
        UserInfo userInfo = iUserInfoService.loadUserByToken(token);
        if (userInfo == null) {
            throw new Exception("10004");
        }

        String processRemark = null;
        if (in.get("processRemark") != null) {
            processRemark = in.get("processRemark").toString();
        }

        JobStop jobStop = iJobStopService.loadJobStopUnProcess(jobId);

        jobStop.setProcessRemark(processRemark);
        jobStop.setProcessTime(new Date());
        jobStop.setProcessUserId(userInfo.getUserId());
        jobStop.setResult(LogStatus.REJECT);
        iJobStopService.updateJobStop(jobStop);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void acceptStop(Map in) throws Exception {
        /**
         * 1、根据token读取当前用户
         * 2、检查jobId是否progress
         * 3、把stop处理为accept
         * 4、把job改为stopped
         * 5、甲方account增加refund
         * 6、乙方account减少refund
         * 7、甲方honor扣除refund
         * 8、乙方honor扣除refund
         */
        Integer userId;
        //获取当前用户id
        if(in.get("userId")==null){
            String token=in.get("token").toString();
            UserInfo userInfo = iUserInfoService.loadUserByToken(token);
            userId=userInfo.getUserId();
        }else {
            userId=(Integer)in.get("userId");
        }

        //检查任务是否正在进行
        Integer jobId=(Integer)in.get("jobId");
        Job job=iJobService.loadJobByJobIdTiny(jobId);
        if(job.getStatus()!=JobStatus.PROGRESS){
            throw new Exception("10071");
        }

        //任务状态终止
        job.setStatus(JobStatus.STOPPED);
        iJobService.updateJob(job);

        //处理意见
        String processRemark=null;
        if(in.get("processRemark")!=null){
            processRemark=in.get("processRemark").toString();
        }

        //保存终止日志
        JobStop jobStop=iJobStopService.loadJobStopUnProcess(jobId);
        jobStop.setResult(LogStatus.ACCEPT);
        jobStop.setProcessUserId(userId);
        jobStop.setProcessTime(new Date());
        jobStop.setProcessRemark(processRemark);
        iJobStopService.updateJobStop(jobStop);

        //处理refund
        UserInfo userA=iUserInfoService.loadUserByUserId(job.getPartyAId());
        Double refund=jobStop.getRefund();
        userA.setAccount(userA.getAccount()+refund);
        userA.setAccountIn(userA.getAccountIn()+refund);

        UserInfo userB=iUserInfoService.loadUserByUserId(job.getPartyBId());
        userB.setAccount(userB.getAccount()-refund);
        userB.setAccountOut(userB.getAccountOut()+refund);

        //处理honor
        userA.setHonor(userA.getHonor()-refund);
        userA.setHonorOut(userA.getHonorOut()+refund);
        userB.setHonor(userB.getHonor()-refund);
        userB.setHonorOut(userB.getHonorOut()+refund);

        iUserInfoService.updateUser(userA);
        iUserInfoService.updateUser(userB);
    }

    @Override
    public Integer countUnreadStop(Map in) throws Exception {
        Integer userId;
        if(in.get("userId")==null){
            String token=in.get("token").toString();
            UserInfo userInfo = iUserInfoService.loadUserByToken(token);
            userId=userInfo.getUserId();
        }else {
            userId=(Integer)in.get("userId");
        }
        Integer jobId=(Integer)in.get("jobId");
        ArrayList<JobStop> stops=iJobStopService.loadMyUnReadStop(jobId, userId);
        return stops.size();
    }
}

package com.gogoyang.rpgapi.business.job.myJob.stop.service;

import com.gogoyang.rpgapi.framework.constant.JobStatus;
import com.gogoyang.rpgapi.framework.constant.LogStatus;
import com.gogoyang.rpgapi.meta.job.entity.Job;
import com.gogoyang.rpgapi.meta.job.service.IJobService;
import com.gogoyang.rpgapi.meta.stop.entity.JobStop;
import com.gogoyang.rpgapi.meta.stop.service.IJobStopService;
import com.gogoyang.rpgapi.meta.user.entity.UserInfo;
import com.gogoyang.rpgapi.meta.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

@Service
public class StopBusinessService implements IStopBusinessService {
    private final IJobStopService iJobStopService;
    private final IJobService iJobService;
    private final IUserService iUserService;

    @Autowired
    public StopBusinessService(IJobStopService iJobStopService,
                               IJobService iJobService,
                               IUserService iUserService) {
        this.iJobStopService = iJobStopService;
        this.iJobService = iJobService;
        this.iUserService = iUserService;
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
        UserInfo user = iUserService.getUserByToken(token);
        if (user == null) {
            throw new Exception("10004");
        }
        String content=in.get("content").toString();
        Double refund=(Double)in.get("refund");

        jobStop = new JobStop();
        jobStop.setContent(content);
        jobStop.setCreatedTime(new Date());
        jobStop.setCreatedUserId(user.getUserId());
        jobStop.setJobId(jobId);
        jobStop.setRefund(refund);
        iJobStopService.insertJobStop(jobStop);
    }

    @Override
    public Page<JobStop> loadStopList(Map in) throws Exception {
        Integer jobId = (Integer) in.get("jobId");
        Integer pageIndex = (Integer) in.get("pageIndex");
        Integer pageSize = (Integer) in.get("pageSize");
        Page<JobStop> stops = iJobStopService.loadJobStopByJobId(jobId, pageIndex, pageSize);
        for (int i = 0; i < stops.getContent().size(); i++) {
             JobStop stop =stops.getContent().get(i);
            User user=iUserService.getUserByUserId(stop.getCreatedUserId());
            if(user.getRealName()!=null) {
                stops.getContent().get(i).setCreatedUserName(user.getRealName());
            }else {
                stops.getContent().get(i).setCreatedUserName(user.getEmail());
            }
            if(stop.getProcessUserId()!=null){
                user=iUserService.getUserByUserId(stop.getProcessUserId());
                if(user.getRealName()!=null) {
                    stops.getContent().get(i).setProcessUserName(user.getRealName());
                }else {
                    stops.getContent().get(i).setProcessUserName(user.getEmail());
                }
            }
        }
        return stops;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void setStopReadTime(Map in) throws Exception {
        Integer jobId = (Integer) in.get("jobId");
        String token = in.get("token").toString();
        User user = iUserService.getUserByToken(token);
        if (user == null) {
            throw new Exception("10004");
        }
        ArrayList<JobStop> stops = iJobStopService.loadMyUnReadStop(jobId, user.getUserId());

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
        Job job = iJobService.getJobByJobIdTiny(jobId);
        if (job.getStatus() != JobStatus.PROGRESS) {
            throw new Exception("10070");
        }

        String token = in.get("token").toString();
        User user = iUserService.getUserByToken(token);
        if (user == null) {
            throw new Exception("10004");
        }

        String processRemark = null;
        if (in.get("processRemark") != null) {
            processRemark = in.get("processRemark").toString();
        }

        JobStop jobStop = iJobStopService.loadJobStopUnProcess(jobId);

        jobStop.setProcessRemark(processRemark);
        jobStop.setProcessTime(new Date());
        jobStop.setProcessUserId(user.getUserId());
        jobStop.setResult(LogStatus.REJECT);
        iJobStopService.updateJobStop(jobStop);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void acceptStop(Map in) throws Exception {
        /**
         * 1、根据token读取当前用户
         * 2、检查jobId是否progress
         * 3、检查是否自己创建的stop
         * 4、把stop处理为accept
         * 5、把job改为stopped
         * 6、甲方account增加refund
         * 7、乙方account减少refund
         * 8、甲方honor扣除refund
         * 9、乙方honor扣除refund
         */
        Integer userId;
        //获取当前用户id
        if(in.get("userId")==null){
            String token=in.get("token").toString();
            User user = iUserService.getUserByToken(token);
            userId=user.getUserId();
        }else {
            userId=(Integer)in.get("userId");
        }

        //检查任务是否正在进行
        Integer jobId=(Integer)in.get("jobId");
        Job job=iJobService.getJobByJobIdTiny(jobId);
        if(job.getStatus()!=JobStatus.PROGRESS){
            throw new Exception("10071");
        }

        //是否自己创建的stop
        JobStop jobStop=iJobStopService.loadJobStopUnProcess(jobId);
        if(jobStop.getCreatedUserId()==userId){
            throw new Exception("10074");
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
        jobStop.setResult(LogStatus.ACCEPT);
        jobStop.setProcessUserId(userId);
        jobStop.setProcessTime(new Date());
        jobStop.setProcessRemark(processRemark);
        iJobStopService.updateJobStop(jobStop);

        //处理refund
        User userA=iUserService.getUserByUserId(job.getPartyAId());
        Double refund=jobStop.getRefund();
        if(userA.getAccount()==null){
            userA.setAccount(refund);
        }else {
            userA.setAccount(userA.getAccount() + refund);
        }
        if(userA.getAccountIn()==null){
            userA.setAccountIn(refund);
        }else {
            userA.setAccountIn(userA.getAccountIn()+refund);
        }

        User userB=iUserService.getUserByUserId(job.getPartyBId());
        if(userB.getAccount()==null){
            userB.setAccount(-refund);
        }else {
            userB.setAccount(userB.getAccount() - refund);
        }
        if(userB.getAccountOut()==null){
            userB.setAccountOut(refund);
        }else {
            userB.setAccountOut(userB.getAccountOut() + refund);
        }

        //处理honor
        if(userA.getHonor()==null){
            userA.setHonor(refund);
        }else {
            userA.setHonor(userA.getHonor() - refund);
        }
        if(userA.getHonorOut()==null){
            userA.setHonorOut(refund);
        }else {
            userA.setHonorOut(userA.getHonorOut() + refund);
        }

        if(userB.getHonor()==null){
            userB.setHonor(-refund);
        }else {
            userB.setHonor(userB.getHonor() - refund);
        }
        if(userB.getHonorOut()==null){
            userB.setHonorOut(refund);
        }else {
            userB.setHonorOut(userB.getHonorOut() + refund);
        }

        iUserService.update(userA);
        iUserService.update(userB);
    }
}

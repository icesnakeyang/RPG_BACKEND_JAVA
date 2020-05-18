package com.gogoyang.rpgapi.business.job.myJob.complete.service;

import com.gogoyang.rpgapi.framework.common.GogoTools;
import com.gogoyang.rpgapi.framework.common.ICommonBusinessService;
import com.gogoyang.rpgapi.framework.constant.GogoStatus;
import com.gogoyang.rpgapi.framework.constant.HonorType;
import com.gogoyang.rpgapi.framework.constant.JobStatus;
import com.gogoyang.rpgapi.framework.constant.LogStatus;
import com.gogoyang.rpgapi.meta.complete.entity.JobComplete;
import com.gogoyang.rpgapi.meta.complete.service.IJobCompleteService;
import com.gogoyang.rpgapi.meta.honor.entity.Honor;
import com.gogoyang.rpgapi.meta.honor.service.IHonorService;
import com.gogoyang.rpgapi.meta.job.entity.Job;
import com.gogoyang.rpgapi.meta.job.service.IJobService;
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
public class CompleteBusinessService implements ICompleteBusinessService {
    private final IJobCompleteService iJobCompleteService;
    private final IJobService iJobService;
    private final IUserService iUserService;
    private final IHonorService iHonorService;
    private final ICommonBusinessService iCommonBusinessService;

    @Autowired
    public CompleteBusinessService(IJobCompleteService iJobCompleteService,
                                   IJobService iJobService,
                                   IUserService iUserService,
                                   IHonorService iHonorService,
                                   ICommonBusinessService iCommonBusinessService) {
        this.iJobCompleteService = iJobCompleteService;
        this.iJobService = iJobService;
        this.iUserService = iUserService;
        this.iHonorService = iHonorService;
        this.iCommonBusinessService = iCommonBusinessService;
    }

    /**
     * 创建一个新的验收任务
     *
     * @param in
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createJobComplete(Map in) throws Exception {
        String token = in.get("token").toString();
        String jobId =  in.get("jobId").toString();
        String content = in.get("content").toString();

        //job必须是progress状态
        Job job = iJobService.getJobTinyByJobId(jobId);
        if (!job.getStatus().equals(JobStatus.PROGRESS.toString())) {
            throw new Exception("10130");
        }

        UserInfo user = iCommonBusinessService.getUserByToken(token);

        if (!user.getUserId().equals(job.getPartyBId())) {
            throw new Exception("10131");
        }


        /**
         * 用户只能创建一次验收申请，如果当前有未处理的申请，则不能再次创建
         */
        JobComplete jobComplete = iJobCompleteService.getUnprocessComplete(jobId);
        if (jobComplete != null) {
            throw new Exception("10129");
        }

        jobComplete = new JobComplete();
        jobComplete.setCompleteId(GogoTools.UUID());
        jobComplete.setContent(content);
        jobComplete.setCreatedTime(new Date());
        jobComplete.setCreatedUserId(user.getUserId());
        jobComplete.setJobId(jobId);
        jobComplete.setProcessUserId(job.getPartyAId());
        jobComplete.setStatus(LogStatus.PENDING.toString());
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
    public ArrayList<JobComplete> listMyComplete(Map in) throws Exception {
        String token=in.get("token").toString();

        String jobId =  in.get("jobId").toString();
        Integer pageIndex = (Integer) in.get("pageIndex");
        Integer pageSize = (Integer) in.get("pageSize");

        UserInfo userInfo=iCommonBusinessService.getUserByToken(token);

        ArrayList<JobComplete> completes = iJobCompleteService.loadJobCompleteByJobId(jobId, pageIndex, pageSize);
        return completes;
    }

    /**
     * 把我未读的验收任务日志设置为当前阅读
     * 包括：创建的新日志，和已处理但未阅读处理结果的日志
     *
     * @param in
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setCompleteReadTime(Map in) throws Exception {
        String token = in.get("token").toString();
        String jobId =  in.get("jobId").toString();

        UserInfo user = iCommonBusinessService.getUserByToken(token);

        Map qIn = new HashMap();
        qIn.put("userId", user.getUserId());
        qIn.put("jobId", jobId);
        qIn.put("readTime", new Date());
        //设置完成日志的阅读时间
        iJobCompleteService.setJobCompleteReadTime(qIn);

        //设置处理结果的阅读时间
        qIn=new HashMap();
        qIn.put("processReadTime", new Date());
        qIn.put("jobId", jobId);
        qIn.put("userId", user.getUserId());
        iJobCompleteService.setJobCompleteProcessReadTime(qIn);
    }

    /**
     * 拒绝验收
     * 读取所有未处理验收申请
     * 设置成reject
     *
     * @param in
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rejectComplete(Map in) throws Exception {
        String token = in.get("token").toString();
        String jobId =  in.get("jobId").toString();
        String processRemark = (String) in.get("processRemark");

        //job status must be progress
        Job job = iCommonBusinessService.getJobTinyByJobId(jobId);
        //任务必须是progress状态，才能进行完成处理
        if (!job.getStatus().equals(JobStatus.PROGRESS.toString())) {
            throw new Exception("10063");
        }

        //user must login
        UserInfo user = iCommonBusinessService.getUserByToken(token);

        //user must be party A
        if (!user.getUserId().equals(job.getPartyAId())) {
            throw new Exception("10127");
        }

        JobComplete jobComplete = iJobCompleteService.getUnprocessComplete(jobId);
        if(jobComplete==null){
            throw new Exception("30015");
        }
        JobComplete jobComplete1=new JobComplete();
        jobComplete1.setCompleteId(jobComplete.getCompleteId());
        jobComplete1.setStatus(LogStatus.REJECT.toString());
        jobComplete1.setProcessRemark(processRemark);
        jobComplete1.setProcessTime(new Date());
        iJobCompleteService.updateJobComplete(jobComplete1);
    }

    /**
     * 通过任务验收
     *
     * @param in
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void acceptComplete(Map in) throws Exception {
        /**
         * 1、查询未处理的Complete，修改结果为Accept
         * 2、把job的Status修改为Accept
         * 3、给甲方增加honor
         * 4、给乙方增加honor
         * 5、刷新甲方和乙方的userinfo的honor值
         */
        String token = in.get("token").toString();
        String jobId = (String) in.get("jobId");
        String processRemark = (String) in.get("processRemark");

        //首先判断任务状态，只有PROGRESS的任务可以操作完成
        Job job=iCommonBusinessService.getJobTinyByJobId(jobId);
        if (!job.getStatus().equals(JobStatus.PROGRESS.toString())) {
            //只能验收任务状态为进行中的任务
            throw new Exception("30016");
        }

        //读取当前用户
        UserInfo user = iCommonBusinessService.getUserByToken(token);

        //当前用户必须是甲方
        if (!user.getUserId().equals(job.getPartyAId())) {
            throw new Exception("10128");
        }

        //读取完成申请
        JobComplete jobComplete = iJobCompleteService.getUnprocessComplete(jobId);
        if (jobComplete == null) {
            //如果没有申请，则为甲方直接通过验收，需创建一个申请
            jobComplete = new JobComplete();
            jobComplete.setCompleteId(GogoTools.UUID());
            jobComplete.setCreatedUserId(user.getUserId());
            jobComplete.setCreatedTime(new Date());
            jobComplete.setJobId(jobId);
            jobComplete.setReadTime(new Date());
            jobComplete.setProcessUserId(user.getUserId());
            jobComplete.setProcessTime(new Date());
            jobComplete.setProcessRemark(processRemark);
            jobComplete.setStatus(LogStatus.ACCEPT.toString());
            iJobCompleteService.insertJobComplete(jobComplete);
        } else {
            //乙方已经申请，直接处理
            jobComplete.setStatus(LogStatus.ACCEPT.toString());
            jobComplete.setProcessRemark(processRemark);
            jobComplete.setProcessTime(new Date());
            jobComplete.setProcessUserId(user.getUserId());
            iJobCompleteService.updateJobComplete(jobComplete);
        }

        //把job设置为accept
        Job job1=new Job();
        job1.setJobId(job.getJobId());
        job1.setStatus(JobStatus.ACCEPTANCE.toString());
        iJobService.updateJob(job1);

        //给甲方增加honor
        Honor honor = new Honor();
        honor.setHonorId(GogoTools.UUID());
        honor.setCreatedTime(new Date());
        honor.setCreatedUserId(user.getUserId());
        honor.setJobId(job.getJobId());
        honor.setPoint(job.getPrice());
        honor.setType(HonorType.JOB_ACCEPTED.toString());
        honor.setUserId(job.getPartyAId());
        iHonorService.createHonor(honor);
        //刷新甲方honor
        UserInfo userA = iUserService.getUserByUserId(job.getPartyAId());
        UserInfo userAEdit=new UserInfo();
        userAEdit.setUserId(userA.getUserId());
        Double ha = 0.0;
        if (userA != null) {
            if (userA.getHonor() != null) {
                ha = userA.getHonor();
            }
        }
        ha += job.getPrice();
        userAEdit.setHonor(ha);
        userAEdit.setHonorIn(ha);
        iUserService.updateUserInfo(userAEdit);

        //给乙方增加honor
        honor = new Honor();
        honor.setHonorId(GogoTools.UUID());
        honor.setUserId(job.getPartyBId());
        honor.setPoint(job.getPrice());
        honor.setType(HonorType.JOB_ACCEPTED.toString());
        honor.setJobId(job.getJobId());
        honor.setCreatedTime(new Date());
        honor.setUserId(user.getUserId());
        iHonorService.createHonor(honor);
        //刷新乙方Honor
        UserInfo userB = iUserService.getUserByUserId(job.getPartyBId());
        UserInfo userBEdit=new UserInfo();
        userBEdit.setUserId(userB.getUserId());
        Double hb = 0.0;
        if (userB.getHonor() != null) {
            if (userB.getHonor() != null) {
                hb = userB.getHonor();
            }
        }
        hb += job.getPrice();
        userBEdit.setHonor(hb);
        userBEdit.setHonorIn(hb);
        iUserService.updateUserInfo(userBEdit);
    }

    /**
     * read all my acceptance jobs
     *
     * @param in
     * @return
     * @throws Exception
     */
    @Override
    public Map listMyPartyAAcceptJob(Map in) throws Exception {
        String token = in.get("token").toString();
        Integer pageIndex = (Integer) in.get("pageIndex");
        Integer pageSize = (Integer) in.get("pageSize");

        UserInfo user = iCommonBusinessService.getUserByToken(token);

        ArrayList<Job> jobs = iJobService.listMyPartyAAcceptJob(user.getUserId(), pageIndex, pageSize);

        ArrayList list = new ArrayList();

        Map out = new HashMap();
        out.put("jobs", list);
        return out;
    }

    /**
     * 读取我是乙方的所有已验收的任务
     * @param in
     * @return
     * @throws Exception
     */
    @Override
    public Map listMyPartyBAcceptJob(Map in) throws Exception {
        String token = in.get("token").toString();
        Integer pageIndex = (Integer) in.get("pageIndex");
        Integer pageSize = (Integer) in.get("pageSize");

        UserInfo user = iCommonBusinessService.getUserByToken(token);

        ArrayList<Job> jobs=iJobService.listMyPartyBAcceptJob(user.getUserId(), pageIndex, pageSize);

        Map out = new HashMap();
        out.put("jobs", jobs);
        return out;
    }

    /**
     * 设置已验收任务的用户阅读时间
     * @param in
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setAcceptReadTime(Map in) throws Exception {
        String token = in.get("token").toString();
        String completeId=in.get("completeId").toString();

        UserInfo user = iCommonBusinessService.getUserByToken(token);

        ArrayList<JobComplete> jobCompletes = iJobCompleteService.listPartyBUnreadAccept(user.getUserId());

        for (int i = 0; i < jobCompletes.size(); i++) {
            jobCompletes.get(i).setProcessReadTime(new Date());
            iJobCompleteService.updateJobComplete(jobCompletes.get(i));
        }
    }

}

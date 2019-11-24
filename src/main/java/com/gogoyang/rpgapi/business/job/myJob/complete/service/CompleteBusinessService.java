package com.gogoyang.rpgapi.business.job.myJob.complete.service;

import com.gogoyang.rpgapi.business.common.ICommonBusinessService;
import com.gogoyang.rpgapi.framework.constant.HonorType;
import com.gogoyang.rpgapi.framework.constant.JobStatus;
import com.gogoyang.rpgapi.framework.constant.LogStatus;
import com.gogoyang.rpgapi.meta.complete.entity.JobComplete;
import com.gogoyang.rpgapi.meta.complete.service.IJobCompleteService;
import com.gogoyang.rpgapi.meta.honor.entity.Honor;
import com.gogoyang.rpgapi.meta.honor.service.IHonorService;
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
    @Transactional(rollbackOn = Exception.class)
    public void createJobComplete(Map in) throws Exception {
        String token = in.get("token").toString();
        Integer jobId = (Integer) in.get("jobId");
        String content = in.get("content").toString();

        //job必须是progress状态
        Job job = iJobService.getJobByJobIdTiny(jobId);
        if (job.getStatus() != JobStatus.PROGRESS) {
            throw new Exception("10130");
        }

        User user = iUserService.getUserByToken(token);
        if (user == null) {
            throw new Exception("10004");
        }
        if (user.getUserId() != job.getPartyBId()) {
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
    public Page<JobComplete> listMyComplete(Map in) throws Exception {
        Integer jobId = (Integer) in.get("jobId");
        Integer pageIndex = (Integer) in.get("pageIndex");
        Integer pageSize = (Integer) in.get("pageSize");
        Page<JobComplete> completes = iJobCompleteService.loadJobCompleteByJobId(jobId, pageIndex, pageSize);
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
    @Transactional(rollbackOn = Exception.class)
    public void setCompleteReadTime(Map in) throws Exception {
        String token = in.get("token").toString();
        Integer jobId = (Integer) in.get("jobId");

        User user = iCommonBusinessService.getUserByToken(token);

        Map qIn = new HashMap();
        qIn.put("userId", user.getUserId());
        qIn.put("jobId", jobId);
        qIn.put("readTime", new Date());
        //设置完成日志的阅读时间
        iJobCompleteService.setJobCompleteReadTime(qIn);

        //设置处理结果的阅读时间
        qIn.put("processReadTime", new Date());
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
    @Transactional(rollbackOn = Exception.class)
    public void rejectComplete(Map in) throws Exception {
        String token = in.get("token").toString();
        Integer jobId = (Integer) in.get("jobId");
        String processRemark = (String) in.get("processRemark");

        //job status must be progress
        Job job = iJobService.getJobByJobIdTiny(jobId);
        if (job.getStatus() != JobStatus.PROGRESS) {
            throw new Exception("10063");
        }

        //user must login
        User user = iUserService.getUserByToken(token);
        if (user == null) {
            throw new Exception("10004");
        }
        //user must be party A
        if (user.getUserId() != job.getPartyAId()) {
            throw new Exception("10127");
        }

        JobComplete jobComplete = iJobCompleteService.getUnprocessComplete(jobId);
        jobComplete.setResult(LogStatus.REJECT);
        jobComplete.setProcessRemark(processRemark);
        jobComplete.setProcessTime(new Date());
        jobComplete.setProcessUserId(user.getUserId());
        iJobCompleteService.updateJobComplete(jobComplete);
    }

    /**
     * 通过任务验收
     *
     * @param in
     * @throws Exception
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public void acceptComplete(Map in) throws Exception {
        /**
         * 1、查询未处理的Complete，修改结果为Accept
         * 2、把job的Status修改为Accept
         * 3、给甲方增加honor
         * 4、给乙方增加honor
         * 5、刷新甲方和乙方的userinfo的honor值
         */
        String token = in.get("token").toString();
        Integer jobId = (Integer) in.get("jobId");
        String processRemark = (String) in.get("processRemark");

        //首先判断任务是否已经验收
        Job job = iJobService.getJobByJobIdTiny(jobId);
        if (job.getStatus() != JobStatus.PROGRESS) {
            throw new Exception("10063");
        }

        //读取当前用户
        User user = iUserService.getUserByToken(token);
        if (user == null) {
            throw new Exception("10004");
        }

        //当前用户必须是甲方
        if (user.getUserId() != job.getPartyAId()) {
            throw new Exception("10128");
        }
        //读取完成申请
        JobComplete jobComplete = iJobCompleteService.getUnprocessComplete(jobId);
        if (jobComplete == null) {
            //如果没有申请，则为甲方直接通过验收，需创建一个申请
            jobComplete = new JobComplete();
            jobComplete.setCreatedUserId(user.getUserId());
            jobComplete.setCreatedTime(new Date());
            jobComplete.setJobId(jobId);
            jobComplete.setReadTime(new Date());
            jobComplete.setProcessUserId(user.getUserId());
            jobComplete.setProcessTime(new Date());
            jobComplete.setProcessRemark(processRemark);
            jobComplete.setResult(LogStatus.ACCEPT);
            iJobCompleteService.insertJobComplete(jobComplete);
        } else {
            //乙方已经申请，直接处理
            jobComplete.setResult(LogStatus.ACCEPT);
            jobComplete.setProcessRemark(processRemark);
            jobComplete.setProcessTime(new Date());
            jobComplete.setProcessUserId(user.getUserId());
            iJobCompleteService.updateJobComplete(jobComplete);
        }

        //把job设置为accept
        job.setStatus(JobStatus.ACCEPTANCE);
        iJobService.updateJob(job);

        //给甲方增加honor
        Honor honor = new Honor();
        honor.setCreatedTime(new Date());
        honor.setCreatedUserId(user.getUserId());
        honor.setJobId(job.getJobId());
        honor.setPoint(job.getPrice());
        honor.setType(HonorType.JOB_ACCEPTED);
        honor.setUserId(job.getPartyAId());
        iHonorService.insertHonor(honor);
        //刷新甲方honor
        User userA = iUserService.getUserByUserId(job.getPartyAId());
        Double ha = 0.0;
        if (userA != null) {
            if (userA.getHonor() != null) {
                ha = userA.getHonor();
            }
        }
        ha += job.getPrice();
        userA.setHonor(ha);
        userA.setHonorIn(ha);
        iUserService.update(userA);

        //给乙方增加honor
        honor = new Honor();
        honor.setUserId(job.getPartyBId());
        honor.setPoint(job.getPrice());
        honor.setType(HonorType.JOB_ACCEPTED);
        honor.setJobId(job.getJobId());
        honor.setCreatedTime(new Date());
        honor.setUserId(user.getUserId());
        iHonorService.insertHonor(honor);
        //刷新乙方Honor
        User userB = iUserService.getUserByUserId(job.getPartyBId());
        Double hb = 0.0;
        if (userB.getHonor() != null) {
            if (userB.getHonor() != null) {
                hb = userB.getHonor();
            }
        }
        hb += job.getPrice();
        userB.setHonor(hb);
        userB.setHonorIn(hb);
        iUserService.update(userB);
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

        User user = iUserService.getUserByToken(token);
        if (user == null) {
            throw new Exception("10004");
        }

        Page<Job> jobs = iJobService.listMyPartyAAcceptJob(user.getUserId(), pageIndex, pageSize);

        ArrayList list = new ArrayList();
        for (int i = 0; i < jobs.getContent().size(); i++) {
            Map map = fillAcceptJobMap(jobs.getContent().get(i));
            list.add(map);
        }
        Map out = new HashMap();
        out.put("jobs", list);
        return out;
    }

    @Override
    public Map listMyPartyBAcceptJob(Map in) throws Exception {
        String token = in.get("token").toString();
        Integer pageIndex = (Integer) in.get("pageIndex");
        Integer pageSize = (Integer) in.get("pageSize");

        User user = iUserService.getUserByToken(token);
        if (user == null) {
            throw new Exception("10004");
        }

        Page<Job> jobs = iJobService.listMyPartyBAcceptJob(user.getUserId(), pageIndex, pageSize);
        ArrayList list = new ArrayList();
        for (int i = 0; i < jobs.getContent().size(); i++) {
            Map map = fillAcceptJobMap(jobs.getContent().get(i));
            list.add(map);
        }
        Map out = new HashMap();
        out.put("jobs", list);
        return out;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void setAcceptReadTime(Map in) throws Exception {
        String token = in.get("token").toString();
        User user = iUserService.getUserByToken(token);
        if (user == null) {
            throw new Exception("10004");
        }

        ArrayList<JobComplete> jobCompletes = iJobCompleteService.listPartyBUnreadAccept(user.getUserId());
        for (int i = 0; i < jobCompletes.size(); i++) {
            jobCompletes.get(i).setProcessReadTime(new Date());
            iJobCompleteService.updateJobComplete(jobCompletes.get(i));
        }
    }

    private Map fillAcceptJobMap(Job job) throws Exception {
        Map map = new HashMap();
        map.put("title", job.getTitle());
        map.put("jobId", job.getJobId());
        map.put("code", job.getCode());
        map.put("partyAId", job.getPartyAId());
        User userA = iUserService.getUserByUserId(job.getPartyAId());
        if (userA.getRealName() != null) {
            map.put("partyAName", userA.getRealName());
        } else {
            map.put("partyAName", userA.getEmail());
        }
        map.put("partyBId", job.getPartyBId());
        User userB = iUserService.getUserByUserId(job.getPartyBId());
        if (userB.getRealName() != null) {
            map.put("partyBName", userB.getRealName());
        } else {
            map.put("partyBName", userB.getEmail());
        }
        map.put("contractTime", job.getContractTime());
        map.put("price", job.getPrice());
        map.put("days", job.getDays());
        JobComplete jobComplete = iJobCompleteService.getCompleteByStatus(job.getJobId(), LogStatus.ACCEPT);
        map.put("acceptedTime", jobComplete.getProcessTime());
        return map;
    }
}

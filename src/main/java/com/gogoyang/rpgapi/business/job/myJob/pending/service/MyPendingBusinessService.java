package com.gogoyang.rpgapi.business.job.myJob.pending.service;

import com.gogoyang.rpgapi.framework.constant.JobStatus;
import com.gogoyang.rpgapi.meta.apply.entity.JobApply;
import com.gogoyang.rpgapi.meta.apply.service.IJobApplyService;
import com.gogoyang.rpgapi.meta.job.entity.Job;
import com.gogoyang.rpgapi.meta.job.entity.JobDetail;
import com.gogoyang.rpgapi.meta.job.service.IJobService;
import com.gogoyang.rpgapi.meta.user.entity.UserInfo;
import com.gogoyang.rpgapi.meta.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class MyPendingBusinessService implements IMyPendingBusinessService {
    private final IJobService iJobService;
    private final IUserService iUserService;
    private final IJobApplyService iJobApplyService;

    @Autowired
    public MyPendingBusinessService(IJobService iJobService,
                                    IUserService iUserService,
                                    IJobApplyService iJobApplyService) {
        this.iJobService = iJobService;
        this.iUserService = iUserService;
        this.iJobApplyService = iJobApplyService;
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
    @Transactional(rollbackOn = Exception.class)
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

        Job job = iJobService.getJobByJobId(jobId);

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
    @Transactional(rollbackOn = Exception.class)
    public void deletePendingJob(Map in) throws Exception {
        String token = in.get("token").toString();
        String jobId = (String) in.get("jobId");

        //read current user
        UserInfo user = iUserService.getUserByToken(token);
        if (user == null) {
            //no user
            throw new Exception("10004");
        }
        //read common
        Job job = iJobService.getJobByJobIdTiny(jobId);
        if (job == null) {
            //no common
            throw new Exception("10100");
        }
        //check the common status must be PENDING
        if (!job.getStatus().equals(JobStatus.PENDING)) {
            if(job.getStatus().equals(JobStatus.MATCHING)){
                ArrayList<JobApply> jobApplies=iJobApplyService.listJobApplyByNotProcesJobId(job.getJobId());
                if(jobApplies.size()>0){
                    throw new Exception("10104");
                }
            }else {
                throw new Exception("10104");
            }
        }
        //check the party a of this common must be current user
        if (job.getPartyAId().intValue() != user.getUserId().intValue()) {
            throw new Exception("10105");
        }
        //delete
        iJobService.deleteJob(jobId);
    }

    @Override
    public Map getMyPendingJob(Map in) throws Exception {
        String token=in.get("token").toString();
        Integer jobId=(Integer)in.get("jobId");

        User user=iUserService.getUserByToken(token);
        if(user==null){
            throw new Exception("10004");
        }

        Job job=iJobService.getJobByJobId(jobId);

        User userA=iUserService.getUserByUserId(job.getPartyAId());
        if(userA.getRealName()!=null) {
            job.setPartyAName(userA.getRealName());
        }else{
            job.setPartyAName(userA.getEmail());
        }

        job.setJobApplyNum(iJobApplyService.countApplyUsers(job.getJobId()));

        Map out=new HashMap();
        out.put("job", job);
        return out;
    }
}

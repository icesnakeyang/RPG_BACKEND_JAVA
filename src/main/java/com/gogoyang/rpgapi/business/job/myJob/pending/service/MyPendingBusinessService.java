package com.gogoyang.rpgapi.business.job.myJob.pending.service;

import com.gogoyang.rpgapi.framework.constant.JobStatus;
import com.gogoyang.rpgapi.meta.job.entity.Job;
import com.gogoyang.rpgapi.meta.job.entity.JobDetail;
import com.gogoyang.rpgapi.meta.job.service.IJobDetail;
import com.gogoyang.rpgapi.meta.job.service.IJobService;
import com.gogoyang.rpgapi.meta.user.entity.User;
import com.gogoyang.rpgapi.meta.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

@Service
public class MyPendingBusinessService implements IMyPendingBusinessService {
    private final IJobService iJobService;
    private final IJobDetail iJobDetail;
    private final IUserService iUserService;

    @Autowired
    public MyPendingBusinessService(IJobService iJobService,
                                    IJobDetail iJobDetail,
                                    IUserService iUserService) {
        this.iJobService = iJobService;
        this.iJobDetail = iJobDetail;
        this.iUserService = iUserService;
    }

    @Override
    public Map listMyPendingJob(Map in) throws Exception {
        String token = in.get("token").toString();
        User user = iUserService.getUserByToken(token);
        Integer partyAId = user.getUserId();
        Integer pageIndex = (Integer) in.get("pageIndex");
        Integer pageSize = (Integer) in.get("pageSize");
        Page<Job> jobs = iJobService.listMyPendingJob(partyAId, pageIndex, pageSize);
//        ArrayList<Job> jobs=iJobService.listMyPendingJob(partyAId, JobStatus.PENDING, pageIndex, pageSize);
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
        Integer jobId = (Integer) in.get("jobId");
        String title = in.get("title").toString();
        String code = in.get("code").toString();
        Integer days = (Integer) in.get("days");
        Double price = (Double) in.get("price");
        String jobDetail = in.get("jobDetail").toString();

        User user = iUserService.getUserByToken(token);

        if (user == null) {
            throw new Exception("10004");
        }

        Job job = iJobService.getJobByJobId(jobId);

        if (job == null) {
            throw new Exception("10100");
        }

        if (job.getStatus() != JobStatus.PENDING) {
            throw new Exception("10101");
        }

        if (user.getUserId().intValue() != job.getPartyAId().intValue()) {
            throw new Exception("10102");
        }

        job.setTitle(title);
        job.setCode(code);
        job.setDays(days);
        job.setPrice(price);
        job.setDetail(jobDetail);
        iJobService.updateJob(job);
        JobDetail detail = iJobDetail.getJobDetailByJobId(jobId);
        detail.setDetail(jobDetail);
        iJobDetail.updateJobDetail(detail);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void deletePendingJob(Map in) throws Exception {
        String token = in.get("token").toString();
        Integer jobId = (Integer) in.get("jobId");

        //read current user
        User user = iUserService.getUserByToken(token);
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
        if (job.getStatus() != JobStatus.PENDING) {
            throw new Exception("10104");
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

        Map out=new HashMap();
        out.put("job", job);
        return out;
    }
}

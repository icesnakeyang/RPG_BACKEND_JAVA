package com.gogoyang.rpgapi.business.job.myPending.service;

import com.gogoyang.rpgapi.framework.constant.JobStatus;
import com.gogoyang.rpgapi.meta.job.entity.Job;
import com.gogoyang.rpgapi.meta.job.entity.JobDetail;
import com.gogoyang.rpgapi.meta.job.service.IJobDetail;
import com.gogoyang.rpgapi.meta.job.service.IJobService;
import com.gogoyang.rpgapi.meta.user.userInfo.entity.UserInfo;
import com.gogoyang.rpgapi.meta.user.userInfo.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class MyPendingBusinessService implements IMyPendingBusinessService{
    private final IUserInfoService iUserInfoService;
    private final IJobService iJobService;
    private final IJobDetail iJobDetail;

    @Autowired
    public MyPendingBusinessService(IJobService iJobService,
                                    IUserInfoService iUserInfoService,
                                    IJobDetail iJobDetail) {
        this.iJobService = iJobService;
        this.iUserInfoService = iUserInfoService;
        this.iJobDetail = iJobDetail;
    }

    @Override
    public Map listMyPendingJob(Map in) throws Exception {
        String token=in.get("token").toString();
        UserInfo userInfo=iUserInfoService.loadUserByToken(token);
        Integer partyAId=userInfo.getUserId();
        Integer pageIndex=(Integer)in.get("pageIndex");
        Integer pageSize=(Integer)in.get("pageSize");
        Page<Job> jobs=iJobService.listMyPendingJob(partyAId, JobStatus.PENDING, pageIndex, pageSize);
//        ArrayList<Job> jobs=iJobService.listMyPendingJob(partyAId, JobStatus.PENDING, pageIndex, pageSize);
        Map out=new HashMap();
        out.put("jobs", jobs);
        return out;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void updateJob(Map in) throws Exception {
        String token=in.get("token").toString();
        Integer jobId=(Integer)in.get("jobId");
        String title=in.get("title").toString();
        String code=in.get("code").toString();
        Integer days=(Integer)in.get("days");
        Double price=(Double)in.get("price");
        String jobDetail=in.get("jobDetail").toString();

        UserInfo userInfo=iUserInfoService.loadUserByToken(token);

        if(userInfo==null){
            throw new Exception("10004");
        }

        Job job=iJobService.loadJobByJobId(jobId);

        if(job==null){
            throw new Exception("10100");
        }

        if(job.getStatus()!=JobStatus.PENDING){
            throw new Exception("10101");
        }

        job.setTitle(title);
        job.setCode(code);
        job.setDays(days);
        job.setPrice(price);
        job.setDetail(jobDetail);
        iJobService.updateJob(job);
        JobDetail detail=iJobDetail.getJobDetailByJobId(jobId);
        detail.setDetail(jobDetail);
        iJobDetail.updateJobDetail(detail);
    }
}

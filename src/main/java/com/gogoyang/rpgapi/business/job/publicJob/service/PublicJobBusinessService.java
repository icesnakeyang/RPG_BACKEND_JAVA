package com.gogoyang.rpgapi.business.job.publicJob.service;

import com.gogoyang.rpgapi.meta.apply.service.IJobApplyService;
import com.gogoyang.rpgapi.meta.job.entity.Job;
import com.gogoyang.rpgapi.meta.job.service.IJobService;
import com.gogoyang.rpgapi.meta.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class PublicJobBusinessService implements IPublicJobBusinessService{
    private final IUserService iUserService;
    private final IJobService iJobService;
    private final IJobApplyService iJobApplyService;

    @Autowired
    public PublicJobBusinessService(IUserService iUserService,
                                    IJobService iJobService,
                                    IJobApplyService iJobApplyService) {
        this.iUserService = iUserService;
        this.iJobService = iJobService;
        this.iJobApplyService = iJobApplyService;
    }

    @Override
    public Map listPublicJob(Map in) throws Exception {
        Integer pageIndex=(Integer)in.get("pageIndex");
        Integer pageSize=(Integer)in.get("pageSize");

        ArrayList<Job> jobPage= iJobService.listPublicJob(pageIndex, pageSize);

        Map out=new HashMap();
        out.put("jobs", jobPage);
        return out;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map getJobDetail(Map in) throws Exception {
        String jobId=in.get("jobId").toString();
        Job job = iJobService.getJobDetailByJobId(jobId);
        Map out=new HashMap();
        out.put("job", job);
        return out;
    }
}

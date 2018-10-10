package com.gogoyang.rpgapi.business.jobPlaza.service;

import com.gogoyang.rpgapi.framework.constant.JobStatus;
import com.gogoyang.rpgapi.meta.job.entity.Job;
import com.gogoyang.rpgapi.meta.job.service.IJobService;
import com.gogoyang.rpgapi.meta.match.service.IJobMatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class JobPlazaBusinessService implements IJobPlazaBusinessService{
    private IJobMatchService iJobMatchService;
    private final IJobService iJobService;

    @Autowired
    public JobPlazaBusinessService(IJobService iJobService) {
        this.iJobService = iJobService;
    }

    @Override
    public Map loadPublicJob(Map in) throws Exception {
        Integer pageIndex=(Integer)in.get("pageIndex");
        Integer pageSize=(Integer)in.get("pageSize");
        Page<Job> jobPage= iJobService.loadJobByStatus(JobStatus.MATCHING,pageIndex, pageSize);
        Map out=new HashMap();
        out.put("jobs", jobPage);
        return out;
    }
}

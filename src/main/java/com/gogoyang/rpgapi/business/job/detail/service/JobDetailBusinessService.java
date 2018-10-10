package com.gogoyang.rpgapi.business.job.detail.service;

import com.gogoyang.rpgapi.meta.job.entity.Job;
import com.gogoyang.rpgapi.meta.job.service.IJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class JobDetailBusinessService implements IJobDetailBusinessService{
    private final IJobService iJobService;

    @Autowired
    public JobDetailBusinessService(IJobService iJobService) {
        this.iJobService = iJobService;
    }

    @Override
    public Map loadJobDetail(Map in) throws Exception {
        Integer jobId=(Integer)in.get("jobId");
        Job job = iJobService.loadJobByJobId(jobId);
        Map out=new HashMap();
        out.put("job", job);
        return out;
    }
}

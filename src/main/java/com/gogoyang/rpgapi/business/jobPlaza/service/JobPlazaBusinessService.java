package com.gogoyang.rpgapi.business.jobPlaza.service;

import com.gogoyang.rpgapi.framework.constant.JobStatus;
import com.gogoyang.rpgapi.meta.job.entity.Job;
import com.gogoyang.rpgapi.meta.job.service.IJobService;
import com.gogoyang.rpgapi.meta.match.service.IJobMatchService;
import com.gogoyang.rpgapi.meta.user.userInfo.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class JobPlazaBusinessService implements IJobPlazaBusinessService{
    private final IUserInfoService iUserInfoService;
    private IJobMatchService iJobMatchService;
    private final IJobService iJobService;

    @Autowired
    public JobPlazaBusinessService(IJobService iJobService, IUserInfoService iUserInfoService) {
        this.iJobService = iJobService;
        this.iUserInfoService = iUserInfoService;
    }

    @Override
    public Map loadPublicJob(Map in) throws Exception {
        Integer pageIndex=(Integer)in.get("pageIndex");
        Integer pageSize=(Integer)in.get("pageSize");
        Map qIn=new HashMap();
        qIn.put("type", "plaza");
        qIn.put("pageIndex", pageIndex);
        qIn.put("pageSize", pageSize);
        Page<Job> jobPage= iJobService.loadJobByStausMap(qIn);
        for(int i=0;i<jobPage.getContent().size();i++){
            jobPage.getContent().get(i).setPartyAName(iUserInfoService.getUserName(
                    jobPage.getContent().get(i).getPartyAId()));
            jobPage.getContent().get(i).setJobApplyNum(
                    jobPage.getContent().get(i).getJobApplyNum());
            jobPage.getContent().get(i).setJobMatchNum(
                    jobPage.getContent().get(i).getJobMatchNum());
        }
        Map out=new HashMap();
        out.put("jobs", jobPage);
        return out;
    }
}

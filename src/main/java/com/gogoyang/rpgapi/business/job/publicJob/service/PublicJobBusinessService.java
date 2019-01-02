package com.gogoyang.rpgapi.business.job.publicJob.service;

import com.gogoyang.rpgapi.meta.apply.service.IJobApplyService;
import com.gogoyang.rpgapi.meta.job.entity.Job;
import com.gogoyang.rpgapi.meta.job.service.IJobService;
import com.gogoyang.rpgapi.meta.user.entity.User;
import com.gogoyang.rpgapi.meta.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

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
        Map qIn=new HashMap();
        qIn.put("pageIndex", pageIndex);
        qIn.put("pageSize", pageSize);
        Page<Job> jobPage= iJobService.listPublicJob(qIn);
        for(int i=0;i<jobPage.getContent().size();i++){
            Job job=jobPage.getContent().get(i);
            User user=iUserService.getUserByUserId(job.getPartyAId());
            if(user.getRealName()!=null){
                jobPage.getContent().get(i).setPartyAName(user.getRealName());
            }else{
                jobPage.getContent().get(i).setPartyAName(user.getEmail());
            }
        }
        Map out=new HashMap();
        out.put("jobs", jobPage);
        return out;
    }

    @Override
    public Map getJobDetail(Map in) throws Exception {
        Integer jobId=(Integer)in.get("jobId");
        Job job = iJobService.getJobByJobId(jobId);
        User partyA=iUserService.getUserByUserId(job.getPartyAId());
        if(partyA.getRealName()!=null) {
            job.setPartyAName(partyA.getRealName());
        }else{
            job.setPartyAName(partyA.getEmail());
        }
        job.setJobApplyNum(iJobApplyService.countApplyUsers(job.getJobId()));
        Map out=new HashMap();
        out.put("job", job);
        return out;
    }
}

package com.gogoyang.rpgapi.business.jobPlaza.service;

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
public class JobPlazaBusinessService implements IJobPlazaBusinessService{
    private final IJobService iJobService;
    private final IUserService iUserService;

    @Autowired
    public JobPlazaBusinessService(IJobService iJobService, IUserService iUserService) {
        this.iJobService = iJobService;
        this.iUserService = iUserService;
    }

    @Override
    public Map listPublicJob(Map in) throws Exception {
        Integer pageIndex=(Integer)in.get("pageIndex");
        Integer pageSize=(Integer)in.get("pageSize");
        Map qIn=new HashMap();
        qIn.put("type", "plaza");
        qIn.put("pageIndex", pageIndex);
        qIn.put("pageSize", pageSize);
        Page<Job> jobPage= iJobService.listJobByStausMap(qIn);
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
}

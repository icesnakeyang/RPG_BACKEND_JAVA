package com.gogoyang.rpgapi.business.job.myJob.partyA.service;

import com.gogoyang.rpgapi.business.job.myJob.common.service.IMyJobCommonBusinessService;
import com.gogoyang.rpgapi.business.job.myJob.complete.service.ICompleteBusinessService;
import com.gogoyang.rpgapi.business.job.myJob.log.service.IMyLogBusinessService;
import com.gogoyang.rpgapi.business.job.myJob.stop.service.IStopBusinessService;
import com.gogoyang.rpgapi.framework.constant.JobStatus;
import com.gogoyang.rpgapi.meta.job.entity.Job;
import com.gogoyang.rpgapi.meta.realname.entity.RealName;
import com.gogoyang.rpgapi.meta.realname.service.IRealNameService;
import com.gogoyang.rpgapi.meta.user.entity.User;
import com.gogoyang.rpgapi.meta.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PartyABusinessService implements IPartyABusinessService{
    private final IJobService iJobService;
    private final ICompleteBusinessService iCompleteBusinessService;
    private final IStopBusinessService iStopBusinessService;
    private final IUserService iUserService;
    private final IRealNameService iRealNameService;
    private final IMyLogBusinessService iMyLogBusinessService;
    private final IMyJobCommonBusinessService iMyJobCommonBusinessService;

    @Autowired
    public PartyABusinessService(IJobService iJobService,
                                 ICompleteBusinessService iCompleteBusinessService,
                                 IStopBusinessService iStopBusinessService,
                                 IUserService iUserService,
                                 IRealNameService iRealNameService,
                                 IMyLogBusinessService iMyLogBusinessService,
                                 IMyJobCommonBusinessService iMyJobCommonBusinessService) {
        this.iJobService = iJobService;
        this.iCompleteBusinessService = iCompleteBusinessService;
        this.iStopBusinessService = iStopBusinessService;
        this.iUserService = iUserService;
        this.iRealNameService = iRealNameService;
        this.iMyLogBusinessService = iMyLogBusinessService;
        this.iMyJobCommonBusinessService = iMyJobCommonBusinessService;
    }

    @Override
    public Page<Job> listMyPartyAJob(Map in) throws Exception {
        String token=in.get("token").toString();

        User user=iUserService.getUserByToken(token);
        if(user==null){
            throw new Exception("10004");
        }

        Page<Job> jobPage=iJobService.listPartyAJob(user.getUserId(),JobStatus.PROGRESS, 0, 100);

        for(int i=0;i<jobPage.getContent().size();i++){
            Job job=jobPage.getContent().get(i);
            Integer partyAId=job.getPartyAId();
            RealName realName=iRealNameService.getRealNameByUserId(partyAId);
            String theName=realName.getRealName();
            jobPage.getContent().get(i).setPartyAName(theName);
            if(jobPage.getContent().get(i).getPartyBId()!=null){
                Integer partyBId=job.getPartyBId();
                realName=iRealNameService.getRealNameByUserId(partyBId);
                theName=realName.getRealName();
                jobPage.getContent().get(i).setPartyBName(theName);
                Integer unread=0;
                unread=iMyJobCommonBusinessService.totalUnreadOneJob(token, jobPage.getContent().get(i).getJobId());
                jobPage.getContent().get(i).setUnRead(unread);
            }
        }

        return jobPage;
    }

    @Override
    public Map getPartyAJob(Map in) throws Exception {
        String token=in.get("token").toString();
        Integer jobId=(Integer)in.get("jobId");

        User user=iUserService.getUserByToken(token);
        if(user==null){
            throw new Exception("10004");
        }

        Job job=iJobService.getJobByJobId(jobId);

        Map out=new HashMap();
        out.put("jobId", job.getJobId());
        out.put("title", job.getTitle());
        out.put("code", job.getCode());
        out.put("price", job.getPrice());
        out.put("days", job.getDays());
        out.put("publishTime", job.getCreatedTime());
        out.put("contractTime", job.getContractTime());
        out.put("partyAId", job.getPartyAId());
        out.put("partyBId", job.getPartyBId());
        out.put("partyAName", user.getRealName());
        User userB=iUserService.getUserByUserId(job.getPartyBId());
        if(userB!=null) {
            out.put("partyBName", userB.getRealName());
        }
        out.put("detail", job.getDetail());
        return out;
    }
}

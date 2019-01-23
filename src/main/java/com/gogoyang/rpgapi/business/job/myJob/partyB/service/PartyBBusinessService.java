package com.gogoyang.rpgapi.business.job.myJob.partyB.service;

import com.gogoyang.rpgapi.business.job.myJob.complete.service.ICompleteBusinessService;
import com.gogoyang.rpgapi.business.job.myJob.log.service.IMyLogBusinessService;
import com.gogoyang.rpgapi.business.job.myJob.stop.service.IStopBusinessService;
import com.gogoyang.rpgapi.framework.constant.JobStatus;
import com.gogoyang.rpgapi.meta.apply.entity.JobApply;
import com.gogoyang.rpgapi.meta.apply.service.IJobApplyService;
import com.gogoyang.rpgapi.meta.job.entity.Job;
import com.gogoyang.rpgapi.meta.job.service.IJobService;
import com.gogoyang.rpgapi.meta.realname.service.IRealNameService;
import com.gogoyang.rpgapi.meta.user.entity.User;
import com.gogoyang.rpgapi.meta.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class PartyBBusinessService implements IPartyBBusinessService {
    private final IJobService iJobService;
    private final ICompleteBusinessService iCompleteBusinessService;
    private final IStopBusinessService iStopBusinessService;
    private final IUserService iUserService;
    private final IRealNameService iRealNameService;
    private final IMyLogBusinessService iMyLogBusinessService;
    private final IJobApplyService iJobApplyService;

    @Autowired
    public PartyBBusinessService(IJobService iJobService,
                                 ICompleteBusinessService iCompleteBusinessService,
                                 IStopBusinessService iStopBusinessService,
                                 IUserService iUserService,
                                 IRealNameService iRealNameService,
                                 IMyLogBusinessService iMyLogBusinessService,
                                 IJobApplyService iJobApplyService) {
        this.iJobService = iJobService;
        this.iCompleteBusinessService = iCompleteBusinessService;
        this.iStopBusinessService = iStopBusinessService;
        this.iUserService = iUserService;
        this.iRealNameService = iRealNameService;
        this.iMyLogBusinessService = iMyLogBusinessService;
        this.iJobApplyService = iJobApplyService;
    }

    @Override
    public Page<Job> listMyPartyBJob(Map in) throws Exception {
        String token = in.get("token").toString();

        User user = iUserService.getUserByToken(token);
        if (user == null) {
            throw new Exception("10004");
        }

        Page<Job> jobPage = iJobService.listPartyBJob(user.getUserId(), JobStatus.PROGRESS, 0, 100);

        for(int i=0;i<jobPage.getContent().size();i++){
            jobPage.getContent().get(i).setPartyAName(iRealNameService.getRealNameByUserId(
                    jobPage.getContent().get(i).getPartyAId()).getRealName());
            jobPage.getContent().get(i).setPartyBName(iRealNameService.getRealNameByUserId(
                    jobPage.getContent().get(i).getPartyBId()).getRealName());
            Integer unread=0;
            Map in2=new HashMap();
            in2.put("userId", user.getUserId());
            in2.put("jobId", jobPage.getContent().get(i).getJobId());
            unread=iMyLogBusinessService.countUnreadJobLog(in2);
            unread+=iCompleteBusinessService.countUnreadComplete(in2);
            unread+=iStopBusinessService.countUnreadStop(in2);
            jobPage.getContent().get(i).setUnRead(unread);
        }

        return jobPage;
    }

    @Override
    public Map getPartyBJobDetail(Map in) throws Exception {
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
        out.put("partyAId", job.getPartyAId());
        out.put("partyBId", job.getPartyBId());
        out.put("createdTime", job.getCreatedTime());
        out.put("contractTime", job.getContractTime());
        out.put("days", job.getDays());
        out.put("price", job.getPrice());
        out.put("detail", job.getDetail());

        User userA=iUserService.getUserByUserId(job.getPartyAId());
        User userB=iUserService.getUserByUserId(job.getPartyBId());
        out.put("partyAName", userA.getRealName());
        out.put("partyBName", userB.getRealName());

        return out;

    }

    @Override
    public Integer totalUnreadNewJob(Map in) throws Exception {
        String token=in.get("token").toString();
        User user=iUserService.getUserByToken(token);
        if(user==null){
            throw new Exception("10004");
        }

        int totalNewJob=0;
        ArrayList<JobApply> jobApplies=iJobApplyService.listPartyBNewJob(user.getUserId());
        totalNewJob=jobApplies.size();
        return totalNewJob;
    }
}

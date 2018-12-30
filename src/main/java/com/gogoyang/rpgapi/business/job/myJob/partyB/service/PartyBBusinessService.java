package com.gogoyang.rpgapi.business.job.myJob.partyB.service;

import com.gogoyang.rpgapi.business.job.complete.service.ICompleteBusinessService;
import com.gogoyang.rpgapi.business.job.jobLog.service.IJobLogBusinessService;
import com.gogoyang.rpgapi.business.job.stop.service.IStopBusinessService;
import com.gogoyang.rpgapi.framework.constant.JobStatus;
import com.gogoyang.rpgapi.meta.job.entity.Job;
import com.gogoyang.rpgapi.meta.job.service.IJobService;
import com.gogoyang.rpgapi.meta.realname.service.IRealNameService;
import com.gogoyang.rpgapi.meta.user.entity.User;
import com.gogoyang.rpgapi.meta.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PartyBBusinessService implements IPartyBBusinessService {
    private final IJobService iJobService;
    private final IJobLogBusinessService iJobLogBusinessService;
    private final ICompleteBusinessService iCompleteBusinessService;
    private final IStopBusinessService iStopBusinessService;
    private final IUserService iUserService;
    private final IRealNameService iRealNameService;

    @Autowired
    public PartyBBusinessService(IJobService iJobService,
                                 IJobLogBusinessService iJobLogBusinessService,
                                 ICompleteBusinessService iCompleteBusinessService,
                                 IStopBusinessService iStopBusinessService,
                                 IUserService iUserService,
                                 IRealNameService iRealNameService) {
        this.iJobService = iJobService;
        this.iJobLogBusinessService = iJobLogBusinessService;
        this.iCompleteBusinessService = iCompleteBusinessService;
        this.iStopBusinessService = iStopBusinessService;
        this.iUserService = iUserService;
        this.iRealNameService = iRealNameService;
    }

    @Override
    public Page<Job> loadMyPartyBJob(Map in) throws Exception {
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
            unread=iJobLogBusinessService.countUnreadJobLog(in2);
            unread+=iCompleteBusinessService.countUnreadComplete(in2);
            unread+=iStopBusinessService.countUnreadStop(in2);
            jobPage.getContent().get(i).setUnRead(unread);
        }

        return jobPage;
    }
}

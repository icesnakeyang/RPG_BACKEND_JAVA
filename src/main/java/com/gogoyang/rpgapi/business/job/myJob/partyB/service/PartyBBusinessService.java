package com.gogoyang.rpgapi.business.job.myJob.partyB.service;

import com.gogoyang.rpgapi.business.job.complete.service.ICompleteBusinessService;
import com.gogoyang.rpgapi.business.job.jobLog.service.IJobLogBusinessService;
import com.gogoyang.rpgapi.business.job.stop.service.IStopBusinessService;
import com.gogoyang.rpgapi.framework.constant.JobStatus;
import com.gogoyang.rpgapi.meta.job.entity.Job;
import com.gogoyang.rpgapi.meta.job.service.IJobService;
import com.gogoyang.rpgapi.meta.user.userInfo.entity.UserInfo;
import com.gogoyang.rpgapi.meta.user.userInfo.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PartyBBusinessService implements IPartyBBusinessService {
    private final IUserInfoService iUserInfoService;
    private final IJobService iJobService;
    private final IJobLogBusinessService iJobLogBusinessService;
    private final ICompleteBusinessService iCompleteBusinessService;
    private final IStopBusinessService iStopBusinessService;

    @Autowired
    public PartyBBusinessService(IUserInfoService iUserInfoService, IJobService iJobService,
                                 IJobLogBusinessService iJobLogBusinessService,
                                 ICompleteBusinessService iCompleteBusinessService, IStopBusinessService iStopBusinessService) {
        this.iUserInfoService = iUserInfoService;
        this.iJobService = iJobService;
        this.iJobLogBusinessService = iJobLogBusinessService;
        this.iCompleteBusinessService = iCompleteBusinessService;
        this.iStopBusinessService = iStopBusinessService;
    }

    @Override
    public Page<Job> loadMyPartyBJob(Map in) throws Exception {
        String token = in.get("token").toString();

        UserInfo userInfo = iUserInfoService.getUserByToken(token);
        if (userInfo == null) {
            throw new Exception("10004");
        }

        Page<Job> jobPage = iJobService.listPartyBJob(userInfo.getUserId(), JobStatus.PROGRESS, 0, 100);

        for(int i=0;i<jobPage.getContent().size();i++){
            jobPage.getContent().get(i).setPartyAName(iUserInfoService.getUserName(
                    jobPage.getContent().get(i).getPartyAId()));
            jobPage.getContent().get(i).setPartyBName(iUserInfoService.getUserName(
                    jobPage.getContent().get(i).getPartyBId()));
            Integer unread=0;
            Map in2=new HashMap();
            in2.put("userId", userInfo.getUserId());
            in2.put("jobId", jobPage.getContent().get(i).getJobId());
            unread=iJobLogBusinessService.countUnreadJobLog(in2);
            unread+=iCompleteBusinessService.countUnreadComplete(in2);
            unread+=iStopBusinessService.countUnreadStop(in2);
            jobPage.getContent().get(i).setUnRead(unread);
        }

        return jobPage;
    }
}

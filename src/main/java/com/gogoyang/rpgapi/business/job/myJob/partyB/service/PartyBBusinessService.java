package com.gogoyang.rpgapi.business.job.myJob.partyB.service;

import com.gogoyang.rpgapi.business.job.myJob.common.service.IMyJobCommonBusinessService;
import com.gogoyang.rpgapi.business.job.myJob.complete.service.ICompleteBusinessService;
import com.gogoyang.rpgapi.business.job.myJob.log.service.IMyLogBusinessService;
import com.gogoyang.rpgapi.business.job.myJob.stop.service.IStopBusinessService;
import com.gogoyang.rpgapi.framework.common.ICommonBusinessService;
import com.gogoyang.rpgapi.framework.constant.JobStatus;
import com.gogoyang.rpgapi.meta.apply.service.IJobApplyService;
import com.gogoyang.rpgapi.meta.job.entity.Job;
import com.gogoyang.rpgapi.meta.job.service.IJobService;
import com.gogoyang.rpgapi.meta.realname.service.IRealNameService;
import com.gogoyang.rpgapi.meta.user.entity.UserInfo;
import com.gogoyang.rpgapi.meta.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class PartyBBusinessService implements IPartyBBusinessService {
    private final IJobService iJobService;
    private final IMyJobCommonBusinessService iMyJobCommonBusinessService;
    private final ICommonBusinessService iCommonBusinessService;

    @Autowired
    public PartyBBusinessService(IJobService iJobService,
                                 IMyJobCommonBusinessService iMyJobCommonBusinessService,
                                 ICommonBusinessService iCommonBusinessService) {
        this.iJobService = iJobService;
        this.iMyJobCommonBusinessService = iMyJobCommonBusinessService;
        this.iCommonBusinessService = iCommonBusinessService;
    }

    @Override
    public ArrayList<Job> listMyPartyBJob(Map in) throws Exception {
        String token = in.get("token").toString();

        UserInfo user = iCommonBusinessService.getUserByToken(token);

        ArrayList<Job> jobList = iJobService.listPartyBJob(user.getUserId(), JobStatus.PROGRESS, 0, 100);

        for(int i=0;i<jobList.size();i++){
            Integer unread=0;
            unread=iMyJobCommonBusinessService.totalUnreadOneJob(token, jobList.get(i).getJobId());
            jobList.get(i).setUnRead(unread);
        }

        return jobList;
    }

    @Override
    public Map getPartyBJobDetail(Map in) throws Exception {
        String token=in.get("token").toString();
        String jobId=in.get("jobId").toString();

        UserInfo user=iCommonBusinessService.getUserByToken(token);

        Job job=iJobService.getJobDetailByJobId(jobId);

        Map out=new HashMap();
        out.put("job",job);
        return out;

    }
}

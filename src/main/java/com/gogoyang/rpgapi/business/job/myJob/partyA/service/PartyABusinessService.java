package com.gogoyang.rpgapi.business.job.myJob.partyA.service;

import com.gogoyang.rpgapi.business.job.myJob.common.service.IMyJobCommonBusinessService;
import com.gogoyang.rpgapi.business.job.myJob.complete.service.ICompleteBusinessService;
import com.gogoyang.rpgapi.business.job.myJob.log.service.IMyLogBusinessService;
import com.gogoyang.rpgapi.business.job.myJob.stop.service.IStopBusinessService;
import com.gogoyang.rpgapi.framework.common.ICommonBusinessService;
import com.gogoyang.rpgapi.framework.constant.JobStatus;
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
public class PartyABusinessService implements IPartyABusinessService {
    private final IJobService iJobService;
    private final ICommonBusinessService iCommonBusinessService;
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
                                 IMyJobCommonBusinessService iMyJobCommonBusinessService,
                                 ICommonBusinessService iCommonBusinessService) {
        this.iJobService = iJobService;
        this.iCompleteBusinessService = iCompleteBusinessService;
        this.iStopBusinessService = iStopBusinessService;
        this.iUserService = iUserService;
        this.iRealNameService = iRealNameService;
        this.iMyLogBusinessService = iMyLogBusinessService;
        this.iMyJobCommonBusinessService = iMyJobCommonBusinessService;
        this.iCommonBusinessService = iCommonBusinessService;
    }

    @Override
    public Map listMyPartyAJob(Map in) throws Exception {
        String token = in.get("token").toString();
        Integer pageIndex = (Integer) in.get("pageIndex");
        Integer pageSize = (Integer) in.get("pageSize");

        UserInfo user = iUserService.getUserByToken(token);
        if (user == null) {
            throw new Exception("10004");
        }

        ArrayList<Job> jobs = iJobService.listPartyAJob(user.getUserId(), JobStatus.PROGRESS, pageIndex, pageSize);

        for (int i = 0; i < jobs.size(); i++) {
            Job job = jobs.get(i);
            Integer unread = 0;
            unread = iMyJobCommonBusinessService.totalUnreadOneJob(token, job.getJobId());
            jobs.get(i).setUnRead(unread);
        }

        Map out = new HashMap();
        out.put("jobs", jobs);

        return out;
    }

    @Override
    public Map getPartyAJob(Map in) throws Exception {
        String token = in.get("token").toString();
        String jobId = in.get("jobId").toString();

        UserInfo user = iCommonBusinessService.getUserByToken(token);

        Job job = iJobService.getJobDetailByJobId(jobId);

        if(!job.getPartyAId().equals(user.getUserId())){
            //当前登录用户不是甲方，没有查看任务权限
            throw new Exception("30003");
        }

        Map out = new HashMap();
        out.put("job", job);

        return out;
    }
}

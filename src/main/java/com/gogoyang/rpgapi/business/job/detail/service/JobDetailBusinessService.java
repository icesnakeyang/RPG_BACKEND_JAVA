package com.gogoyang.rpgapi.business.job.detail.service;

import com.gogoyang.rpgapi.business.job.jobLog.service.IJobLogBusinessService;
import com.gogoyang.rpgapi.meta.job.entity.Job;
import com.gogoyang.rpgapi.meta.job.service.IJobService;
import com.gogoyang.rpgapi.meta.user.userInfo.entity.UserInfo;
import com.gogoyang.rpgapi.meta.user.userInfo.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class JobDetailBusinessService implements IJobDetailBusinessService{
    private final IJobService iJobService;
    private final IJobLogBusinessService iJobLogBusinessService;
    private final IUserInfoService iUserInfoService;

    @Autowired
    public JobDetailBusinessService(IJobService iJobService, IJobLogBusinessService iJobLogBusinessService, IUserInfoService iUserInfoService) {
        this.iJobService = iJobService;
        this.iJobLogBusinessService = iJobLogBusinessService;
        this.iUserInfoService = iUserInfoService;
    }

    @Override
    public Map loadJobDetail(Map in) throws Exception {
        Integer jobId=(Integer)in.get("jobId");
        Job job = iJobService.loadJobByJobId(jobId);
        Map out=new HashMap();
        out.put("job", job);
        return out;
    }

    /**
     * 读取任务详情，包括登录用户的各种信息
     * @param in
     * @return
     * @throws Exception
     */
    @Override
    public Map loadJobDetailLogin(Map in) throws Exception {
        String token=in.get("token").toString();
        Integer jobId=(Integer)in.get("jobId");

        Job job=iJobService.loadJobByJobId(jobId);
        Integer unreadJobLog=iJobLogBusinessService.countUnreadJobLog(in);

        Map out=new HashMap();
        out.put("unreadJobLog", unreadJobLog);
        out.put("job", job);

        return out;
    }
}

package com.gogoyang.rpgapi.business.job.myPending.service;

import com.gogoyang.rpgapi.framework.constant.JobStatus;
import com.gogoyang.rpgapi.meta.job.entity.Job;
import com.gogoyang.rpgapi.meta.job.service.IJobService;
import com.gogoyang.rpgapi.meta.user.userInfo.entity.UserInfo;
import com.gogoyang.rpgapi.meta.user.userInfo.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class MyPendingBusinessService implements IMyPendingBusinessService{
    private final IUserInfoService iUserInfoService;
    private final IJobService iJobService;

    @Autowired
    public MyPendingBusinessService(IJobService iJobService, IUserInfoService iUserInfoService) {
        this.iJobService = iJobService;
        this.iUserInfoService = iUserInfoService;
    }

    @Override
    public Map listMyPendingJob(Map in) throws Exception {
        String token=in.get("token").toString();
        UserInfo userInfo=iUserInfoService.loadUserByToken(token);
        Integer partyAId=userInfo.getUserId();
        Integer pageIndex=(Integer)in.get("pageIndex");
        Integer pageSize=(Integer)in.get("pageSize");
        Page<Job> jobs=iJobService.listMyPendingJob(partyAId, JobStatus.PENDING, pageIndex, pageSize);
//        ArrayList<Job> jobs=iJobService.listMyPendingJob(partyAId, JobStatus.PENDING, pageIndex, pageSize);
        Map out=new HashMap();
        out.put("jobs", jobs);
        return out;
    }
}

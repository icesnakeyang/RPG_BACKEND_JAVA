package com.gogoyang.rpgapi.business.job.myJob.partyB.service;

import com.gogoyang.rpgapi.framework.constant.JobStatus;
import com.gogoyang.rpgapi.meta.job.entity.Job;
import com.gogoyang.rpgapi.meta.job.service.IJobService;
import com.gogoyang.rpgapi.meta.user.userInfo.entity.UserInfo;
import com.gogoyang.rpgapi.meta.user.userInfo.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PartyBBusinessService implements IPartyBBusinessService {
    private final IUserInfoService iUserInfoService;
    private final IJobService iJobService;

    @Autowired
    public PartyBBusinessService(IUserInfoService iUserInfoService, IJobService iJobService) {
        this.iUserInfoService = iUserInfoService;
        this.iJobService = iJobService;
    }

    @Override
    public Page<Job> loadMyPartyBJob(Map in) throws Exception {
        String token = in.get("token").toString();

        UserInfo userInfo = iUserInfoService.loadUserByToken(token);
        if (userInfo == null) {
            throw new Exception("10004");
        }

        Page<Job> jobPage = iJobService.loadPartyBJob(userInfo.getUserId(), JobStatus.PROGRESS, 0, 100);

        return jobPage;
    }
}
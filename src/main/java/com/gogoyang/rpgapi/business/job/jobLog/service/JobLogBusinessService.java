package com.gogoyang.rpgapi.business.job.jobLog.service;

import com.gogoyang.rpgapi.meta.log.entity.JobLog;
import com.gogoyang.rpgapi.meta.log.service.IJobLogService;
import com.gogoyang.rpgapi.meta.user.userInfo.entity.UserInfo;
import com.gogoyang.rpgapi.meta.user.userInfo.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Map;

@Service
public class JobLogBusinessService implements IJobLogBusinessService{
    private final IJobLogService iJobLogService;
    private final IUserInfoService iUserInfoService;

    @Autowired
    public JobLogBusinessService(IJobLogService iJobLogService, IUserInfoService iUserInfoService) {
        this.iJobLogService = iJobLogService;
        this.iUserInfoService = iUserInfoService;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void createLog(Map in) throws Exception {
        String token=in.get("token").toString();
        Integer jobId=(Integer)in.get("jobId");
        String content=in.get("content").toString();

        UserInfo userInfo=iUserInfoService.loadUserByToken(token);
        if(userInfo==null){
            throw new Exception("10004");
        }

        JobLog jobLog=new JobLog();
        jobLog.setContent(content);
        jobLog.setCreatedTime(new Date());
        jobLog.setCreatedUserId(userInfo.getUserId());
        jobLog.setJobId(jobId);
        iJobLogService.createJobLog(jobLog);
    }

    @Override
    public Page<JobLog> loadJobLog(Map in) throws Exception {
        return null;
    }

    @Override
    public void setJobLogReadTime(Map in) throws Exception {
        JobLog jobLog=iJobLogService.load
        iJobLogService.setReadTime()

    }
}

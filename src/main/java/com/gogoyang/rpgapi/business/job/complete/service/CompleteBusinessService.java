package com.gogoyang.rpgapi.business.job.complete.service;

import com.gogoyang.rpgapi.meta.complete.entity.JobComplete;
import com.gogoyang.rpgapi.meta.complete.service.IJobCompleteService;
import com.gogoyang.rpgapi.meta.log.entity.JobLog;
import com.gogoyang.rpgapi.meta.user.userInfo.entity.UserInfo;
import com.gogoyang.rpgapi.meta.user.userInfo.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

@Service
public class CompleteBusinessService implements ICompleteBusinessService {
    private final IJobCompleteService iJobCompleteService;
    private final IUserInfoService iUserInfoService;

    @Autowired
    public CompleteBusinessService(IJobCompleteService iJobCompleteService, IUserInfoService iUserInfoService) {
        this.iJobCompleteService = iJobCompleteService;
        this.iUserInfoService = iUserInfoService;
    }

    /**
     * 创建一个新的验收任务
     *
     * @param in
     * @throws Exception
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public void createJobComplete(Map in) throws Exception {
        String token = in.get("token").toString();
        Integer jobId = (Integer) in.get("jobId");
        String content = in.get("content").toString();

        UserInfo userInfo = iUserInfoService.loadUserByToken(token);
        if (userInfo == null) {
            throw new Exception("10004");
        }

        JobComplete jobComplete = new JobComplete();
        jobComplete.setContent(content);
        jobComplete.setCreatedTime(new Date());
        jobComplete.setCreatedUserId(userInfo.getUserId());
        jobComplete.setJobId(jobId);
        iJobCompleteService.createJobComplete(jobComplete);
    }

    /**
     * 获取任务的所有验收日志
     *
     * @param in
     * @return
     * @throws Exception
     */
    @Override
    public Page<JobComplete> loadCompleteList(Map in) throws Exception {
        Integer jobId = (Integer) in.get("jobId");
        Integer pageIndex = (Integer) in.get("pageIndex");
        Integer pageSize = (Integer) in.get("pageSize");
        Page<JobComplete> completes = iJobCompleteService.loadJobCompleteByJobId(jobId, pageIndex, pageSize);
        return completes;
    }

    /**
     * 把我未读的验收任务日志设置为当前阅读
     *
     * @param in
     * @throws Exception
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public void setCompleteReadTime(Map in) throws Exception {
        Integer jobId = (Integer) in.get("jobId");
        String token = in.get("token").toString();
        UserInfo userInfo = iUserInfoService.loadUserByToken(token);
        if (userInfo == null) {
            throw new Exception("10004");
        }
        ArrayList<JobComplete> jobCompletes = iJobCompleteService.loadMyUnReadComplete(jobId, userInfo.getUserId());

        for (int i = 0; i < jobCompletes.size(); i++) {
            jobCompletes.get(i).setReadTime(new Date());
            iJobCompleteService.updateJobComplete(jobCompletes.get(i));
        }
    }
}

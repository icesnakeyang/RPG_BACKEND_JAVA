package com.gogoyang.rpgapi.business.job.myJob.log.service;

import com.gogoyang.rpgapi.framework.common.ICommonBusinessService;
import com.gogoyang.rpgapi.meta.log.entity.JobLog;
import com.gogoyang.rpgapi.meta.log.service.IJobLogService;
import com.gogoyang.rpgapi.meta.user.entity.UserInfo;
import com.gogoyang.rpgapi.meta.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class MyLogBusinessService implements IMyLogBusinessService {
    private final IJobLogService iJobLogService;
    private final IUserService iUserService;
    private final ICommonBusinessService iCommonBusinessService;

    @Autowired
    public MyLogBusinessService(IJobLogService iJobLogService,
                                IUserService iUserService,
                                ICommonBusinessService iCommonBusinessService) {
        this.iJobLogService = iJobLogService;
        this.iUserService = iUserService;
        this.iCommonBusinessService = iCommonBusinessService;
    }

    /**
     * 创建一个日志
     *
     * @param in
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createLog(Map in) throws Exception {
        String token = in.get("token").toString();
        String jobId =  in.get("jobId").toString();
        String content = in.get("content").toString();

        UserInfo user = iCommonBusinessService.getUserByToken(token);

        JobLog jobLog = new JobLog();
        jobLog.setContent(content);
        jobLog.setCreatedTime(new Date());
        jobLog.setCreatedUserId(user.getUserId());
        jobLog.setJobId(jobId);
        iJobLogService.createJobLog(jobLog);
    }

    /**
     * 读取一个任务的所有日志
     * 分页
     *
     * @param in
     * @return
     * @throws Exception
     */
    @Override
    public ArrayList<JobLog> loadJobLog(Map in) throws Exception {
        String jobId =  in.get("jobId").toString();
        Integer pageIndex = (Integer) in.get("pageIndex");
        Integer pageSize = (Integer) in.get("pageSize");

        ArrayList<JobLog> jobLogs = iJobLogService.loadJobLogByJobId(jobId, pageIndex, pageSize);
        return jobLogs;
    }

    /**
     * 把我未读的任务日志设置为当前阅读
     *
     * @param in
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setJobLogReadTime(Map in) throws Exception {
        String token = in.get("token").toString();
        Integer jobId = (Integer) in.get("jobId");

        UserInfo user = iCommonBusinessService.getUserByToken(token);

        Map qIn = new HashMap();
        qIn.put("readTime", new Date());
        qIn.put("userId", user.getUserId());
        qIn.put("jobId", jobId);

        iJobLogService.setJobLogReadTime(qIn);
    }
}

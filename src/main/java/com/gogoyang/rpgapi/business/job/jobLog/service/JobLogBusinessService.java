package com.gogoyang.rpgapi.business.job.jobLog.service;

import com.gogoyang.rpgapi.meta.log.entity.JobLog;
import com.gogoyang.rpgapi.meta.log.service.IJobLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

@Service
public class JobLogBusinessService implements IJobLogBusinessService{
    private final IJobLogService iJobLogService;

    @Autowired
    public JobLogBusinessService(IJobLogService iJobLogService) {
        this.iJobLogService = iJobLogService;
    }

    /**
     * 创建一个日志
     * @param in
     * @throws Exception
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public void createLog(Map in) throws Exception {
        String token=in.get("token").toString();
        Integer jobId=(Integer)in.get("jobId");
        String content=in.get("content").toString();

        UserInfo userInfo=iUserInfoService.getUserByToken(token);
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

    /**
     * 读取一个任务的所有日志
     * 分页
     * @param in
     * @return
     * @throws Exception
     */
    @Override
    public Page<JobLog> loadJobLog(Map in) throws Exception {
        Integer jobId=(Integer)in.get("jobId");
        Integer pageIndex=(Integer)in.get("pageIndex");
        Integer pageSize=(Integer)in.get("pageSize");

        Page<JobLog> jobLogs=iJobLogService.loadJobLogByJobId(jobId, pageIndex, pageSize);
        return jobLogs;
    }

    /**
     * 把我未读的任务日志设置为当前阅读
     * @param in
     * @throws Exception
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public void setJobLogReadTime(Map in) throws Exception {
        /**
         * 读取所有我未读的日志
         * 逐条设置阅读时间
         */
        Integer jobId=(Integer)in.get("jobId");
        String token=in.get("token").toString();
        UserInfo userInfo=iUserInfoService.getUserByToken(token);
        if(userInfo==null){
            throw new Exception("10004");
        }
        ArrayList<JobLog> jobLogs=iJobLogService.loadMyUnreadJobLog(jobId, userInfo.getUserId());

        for(int i=0;i<jobLogs.size();i++){
            jobLogs.get(i).setReadTime(new Date());
            iJobLogService.updateJobLog(jobLogs.get(i));
        }
    }

    /**
     * 统计userId未读的JobId的日志
     * @param in
     * @return
     * @throws Exception
     */
    @Override
    public Integer countUnreadJobLog(Map in) throws Exception {
        Integer userId;
        if(in.get("userId")==null){
            String token=in.get("token").toString();
            UserInfo userInfo = iUserInfoService.getUserByToken(token);
            userId=userInfo.getUserId();
        }else {
            userId=(Integer)in.get("userId");
        }
        Integer jobId=(Integer)in.get("jobId");
        ArrayList<JobLog> jobLogs=iJobLogService.loadMyUnreadJobLog(jobId, userId);
        return jobLogs.size();
    }
}

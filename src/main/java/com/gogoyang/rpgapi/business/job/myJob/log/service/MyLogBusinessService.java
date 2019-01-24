package com.gogoyang.rpgapi.business.job.myJob.log.service;

import com.gogoyang.rpgapi.meta.log.entity.JobLog;
import com.gogoyang.rpgapi.meta.log.service.IJobLogService;
import com.gogoyang.rpgapi.meta.user.entity.User;
import com.gogoyang.rpgapi.meta.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

@Service
public class MyLogBusinessService implements IMyLogBusinessService{
    private final IJobLogService iJobLogService;
    private final IUserService iUserService;

    @Autowired
    public MyLogBusinessService(IJobLogService iJobLogService,
                                 IUserService iUserService) {
        this.iJobLogService = iJobLogService;
        this.iUserService = iUserService;
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

        User user=iUserService.getUserByToken(token);
        if(user==null){
            throw new Exception("10004");
        }

        JobLog jobLog=new JobLog();
        jobLog.setContent(content);
        jobLog.setCreatedTime(new Date());
        jobLog.setCreatedUserId(user.getUserId());
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
        User user=iUserService.getUserByToken(token);
        if(user==null){
            throw new Exception("10004");
        }

        ArrayList<JobLog> jobLogsA=iJobLogService.listPartyAUnreadJobLogJobId(jobId, user.getUserId());
        for(int i=0;i<jobLogsA.size();i++){
            jobLogsA.get(i).setReadTime(new Date());
            iJobLogService.updateJobLog(jobLogsA.get(i));
        }
        ArrayList<JobLog> jobLogsB=iJobLogService.listPartyBUnreadJobLogJobId(jobId, user.getUserId());
        for(int i=0;i<jobLogsB.size();i++){
            jobLogsB.get(i).setReadTime(new Date());
            iJobLogService.updateJobLog(jobLogsB.get(i));
        }
    }
}

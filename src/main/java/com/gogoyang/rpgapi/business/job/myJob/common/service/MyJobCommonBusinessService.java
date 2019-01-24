package com.gogoyang.rpgapi.business.job.myJob.common.service;

import com.gogoyang.rpgapi.business.job.myJob.complete.service.ICompleteBusinessService;
import com.gogoyang.rpgapi.business.job.myJob.log.service.IMyLogBusinessService;
import com.gogoyang.rpgapi.business.job.myJob.stop.service.IStopBusinessService;
import com.gogoyang.rpgapi.meta.complete.entity.JobComplete;
import com.gogoyang.rpgapi.meta.complete.service.IJobCompleteService;
import com.gogoyang.rpgapi.meta.job.entity.Job;
import com.gogoyang.rpgapi.meta.job.service.IJobService;
import com.gogoyang.rpgapi.meta.log.entity.JobLog;
import com.gogoyang.rpgapi.meta.log.service.IJobLogService;
import com.gogoyang.rpgapi.meta.user.entity.User;
import com.gogoyang.rpgapi.meta.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class MyJobCommonBusinessService implements IMyJobCommonBusinessService {
    private final IMyLogBusinessService iMyLogBusinessService;
    private final ICompleteBusinessService iCompleteBusinessService;
    private final IStopBusinessService iStopBusinessService;
    private final IJobService iJobService;
    private final IUserService iUserService;
    private final IJobLogService iJobLogService;
    private final IJobCompleteService iJobCompleteService;

    @Autowired
    public MyJobCommonBusinessService(IMyLogBusinessService iMyLogBusinessService,
                                      ICompleteBusinessService iCompleteBusinessService,
                                      IStopBusinessService iStopBusinessService,
                                      IJobService iJobService,
                                      IUserService iUserService,
                                      IJobLogService iJobLogService,
                                      IJobCompleteService iJobCompleteService) {
        this.iMyLogBusinessService = iMyLogBusinessService;
        this.iCompleteBusinessService = iCompleteBusinessService;
        this.iStopBusinessService = iStopBusinessService;
        this.iJobService = iJobService;
        this.iUserService = iUserService;
        this.iJobLogService = iJobLogService;
        this.iJobCompleteService = iJobCompleteService;
    }

    /**
     * 读取我的所有任务的各种状态，未读事件
     *
     * @param in
     * @return
     * @throws Exception
     */
    @Override
    public Map totalMyUnread(Map in) throws Exception {
        /**
         * 1、所有我未阅读的jobApply
         * 2、所有我未阅读的jobLog
         * 3、所有我未阅读的jobComplete
         */
        String token = in.get("token").toString();
        Integer jobId=(Integer)in.get("jobId");
        User user=iUserService.getUserByToken(token);
        if(user==null){
            throw new Exception("10004");
        }
        Map out = new HashMap();

        if(jobId==null) {
            out.put("unReadJobLog", countUnreadJobLog(user.getUserId()));
            out.put("unReadComplete", countUnreadComplete(user.getUserId()));
            out.put("unReadStop", countUnreadStop(in));
        }else {
            out.put("unReadJobLog", countUnreadJobLogJobId(user.getUserId(), jobId));
            out.put("unReadComplete", countUnreadCompleteJobId(user.getUserId(), jobId));
        }
        return out;
    }

    /**
     * 统计一个任务里我未阅读的各种更新状态
     */
    public Integer totalUnreadOneJob(String token, Integer jobId) throws Exception {
        Map qIn = new HashMap();
        qIn.put("token", token);
        qIn.put("jobId", jobId);

        Map unreadMap = totalMyUnread(qIn);
        int unread = 0;
        int unReadJobLog = (Integer) unreadMap.get("unReadJobLog");
        unread += unReadJobLog;
        int unReadComplete = (Integer) unreadMap.get("unReadComplete");
        unread += unReadComplete;
        return unread;
    }

    /**
     * 根据taskId获取job
     *
     * @param in
     * @return
     * @throws Exception
     */
    @Override
    public Map getJobTinyByTaskId(Map in) throws Exception {
        Integer taskId = (Integer) in.get("taskId");
        Job job = iJobService.getJobByTaskId(taskId);
        Map out = new HashMap();
        out.put("job", job);
        return out;
    }

    @Override
    public Map getJobTinyByJobId(Map in) throws Exception {
        Integer jobId = (Integer) in.get("jobId");
        Job job = iJobService.getJobByJobIdTiny(jobId);
        Map out = new HashMap();
        out.put("job", job);
        return out;
    }

    /**
     * 统计userId未读的JobId的日志
     */
    private Integer countUnreadJobLog(Integer userId) throws Exception {
        ArrayList<JobLog> jobLogsA=iJobLogService.listPartyAUnreadJobLog(userId);
        ArrayList<JobLog> jobLogsB=iJobLogService.listPartyBUnreadJobLog(userId);
        int unRead=jobLogsA.size()+jobLogsB.size();
        return unRead;
    }

    private Integer countUnreadJobLogJobId(Integer userId, Integer jobId) throws Exception {
        ArrayList<JobLog> jobLogsA=iJobLogService.listPartyAUnreadJobLog(userId);
        ArrayList<JobLog> jobLogsB=iJobLogService.listPartyBUnreadJobLog(userId);
        int unRead=jobLogsA.size()+jobLogsB.size();
        return unRead;
    }

    /**
     * 统计userId未读的complete日志
     */
    private Integer countUnreadComplete(Integer userId) throws Exception {
        /**
         * 如果当前用户是甲方
         * 查询是否有未阅读的验收日志
         * 如果当前用户是乙方
         * 查询是否有未阅读的处理验收日志
         */

        ArrayList<JobComplete> jobCompletes = iJobCompleteService.listPartyAUnread(userId);
        int unread=jobCompletes.size();
        jobCompletes=iJobCompleteService.listPartyBUnread(userId);
        unread+=jobCompletes.size();
        return unread;
    }

    private Integer countUnreadCompleteJobId(Integer userId, Integer jobId) throws Exception {
        /**
         * 如果当前用户是甲方
         * 查询是否有未阅读的验收日志
         * 如果当前用户是乙方
         * 查询是否有未阅读的处理验收日志
         */

        ArrayList<JobComplete> jobCompletes = iJobCompleteService.listPartyAUnreadJobId(userId, jobId);
        int unread=jobCompletes.size();
        jobCompletes=iJobCompleteService.listPartyBUnreadJobId(userId,jobId);
        unread+=jobCompletes.size();
        return unread;
    }

    private Integer countUnreadStop(Map in) throws Exception {

        return 0;
    }
}

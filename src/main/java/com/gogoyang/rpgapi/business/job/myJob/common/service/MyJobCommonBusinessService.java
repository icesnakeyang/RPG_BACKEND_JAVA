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
         * 1、所有我是甲方的未读
         * 2、所有我是乙方的未读
         * 3、所有我是乙方的未读的验收通过日志
         */
        /**
         * 返回：
         * totalUnread
         * totalPartyAUnread
         * totalPartyBUnread
         * totalUnreadAccept
         */
        String token = in.get("token").toString();
        Integer jobId = (Integer) in.get("jobId");
        User user = iUserService.getUserByToken(token);
        if (user == null) {
            throw new Exception("10004");
        }
        Map out = new HashMap();

        if (jobId == null) {
            Integer totalUnread = 0;
            Integer totalPartyAUnread = 0;
            Integer totalPartyBUnread = 0;
            Integer totalUnreadAccept = 0;
            totalPartyAUnread = totalPartyAUnread(user.getUserId());
            totalPartyBUnread += totalPartyBUnread(user.getUserId());
            totalUnreadAccept = totalUnreadAccept(user.getUserId());
            totalUnread = totalPartyAUnread + totalPartyBUnread + totalUnreadAccept;
            out.put("totalUnread", totalUnread);
            out.put("totalPartyAUnread", totalPartyAUnread);
            out.put("totalPartyBUnread", totalPartyBUnread);
            out.put("totalUnreadAccept", totalUnreadAccept);
        } else {
            out.put("unReadJobLog", countUnreadJobLogJobId(user.getUserId(), jobId));
            out.put("unReadComplete", countUnreadCompleteJobId(user.getUserId(), jobId));
        }
        return out;
    }

    private Object countUnreadCompleteJobId(Integer userId, Integer jobId) throws Exception{
        Map qIn = new HashMap();
        qIn.put("jobId", jobId);
        qIn.put("userId", userId);
        Integer totalUnreadJobLog = iJobCompleteService.totalUnreadComplete(qIn);
        return totalUnreadJobLog;
    }

    /**
     * 统计一个任务里我未阅读的各种更新状态
     */
    public Integer totalUnreadOneJob(String token, Integer jobId) throws Exception {
        Map qIn = new HashMap();
        qIn.put("token", token);
        qIn.put("jobId", jobId);

        Map unreadMap = totalMyUnread(qIn);
        Integer unread = 0;
        Integer unReadJobLog = (Integer) unreadMap.get("unReadJobLog");
        if (unReadJobLog == null) {
            unReadJobLog = 0;
        }
        unread += unReadJobLog;
        Integer unReadComplete = (Integer) unreadMap.get("unReadComplete");
        if (unReadComplete == null) {
            unReadComplete = 0;
        }
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

    @Override
    public Map totalUnreadByJobId(Map in) throws Exception {
        Map out = totalMyUnread(in);
        return out;
    }

    private Integer countUnreadJobLogJobId(Integer userId, Integer jobId) throws Exception {
        ArrayList<JobLog> jobLogsA = iJobLogService.listPartyAUnreadJobLog(userId);
        ArrayList<JobLog> jobLogsB = iJobLogService.listPartyBUnreadJobLog(userId);
        int unRead = jobLogsA.size() + jobLogsB.size();
        Integer totalUnreadJobLog = totalUnreadJobLog(userId, jobId);
        if (totalUnreadJobLog == null) {
            totalUnreadJobLog = 0;
        }
        unRead += totalUnreadJobLog;
        return unRead;
    }

    private Integer totalUnreadJobLog(Integer userId, Integer jobId) throws Exception {
        Map qIn = new HashMap();
        qIn.put("jobId", jobId);
        qIn.put("userId", userId);
        Integer totalUnreadJobLog = iJobLogService.totalUnreadLog(qIn);
        return totalUnreadJobLog;
    }

    /**
     * 统计甲方未读日志
     */
    private Integer totalPartyAUnread(Integer userId) throws Exception {
        /**
         * party a unread log
         * party a unread complete
         */
        int unread = 0;
        ArrayList<JobLog> jobLogs = iJobLogService.listPartyAUnreadJobLog(userId);
        unread += jobLogs.size();
        ArrayList<JobComplete> jobCompletes = iJobCompleteService.listPartyAUnread(userId);
        unread += jobCompletes.size();
        return unread;
    }

    /**
     * 统计乙方未读日志
     *
     * @param userId
     * @return
     * @throws Exception
     */
    private Integer totalPartyBUnread(Integer userId) throws Exception {
        /**
         * party b unread complete
         * party b unread job log
         */
        int unread = 0;
        ArrayList<JobComplete> jobCompletes = iJobCompleteService.listPartyBUnread(userId);
        unread += jobCompletes.size();
        ArrayList<JobLog> jobLogs = iJobLogService.listPartyBUnreadJobLog(userId);
        unread += jobLogs.size();
        return unread;
    }

    private Integer totalUnreadAccept(Integer userId) throws Exception {
        /**
         * 只有乙方才有未读的验收日志
         */
        ArrayList<JobComplete> jobCompletes = iJobCompleteService.listPartyBUnreadAccept(userId);
        int unread = jobCompletes.size();
        return unread;
    }

    private Integer countUnreadStop(Map in) throws Exception {

        return 0;
    }
}

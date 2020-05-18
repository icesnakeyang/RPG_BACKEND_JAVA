package com.gogoyang.rpgapi.business.job.common;

import com.gogoyang.rpgapi.framework.common.ICommonBusinessService;
import com.gogoyang.rpgapi.meta.complete.service.IJobCompleteService;
import com.gogoyang.rpgapi.meta.job.entity.Job;
import com.gogoyang.rpgapi.meta.job.service.IJobService;
import com.gogoyang.rpgapi.meta.log.service.IJobLogService;
import com.gogoyang.rpgapi.meta.stop.service.IJobStopService;
import com.gogoyang.rpgapi.meta.user.entity.UserInfo;
import com.gogoyang.rpgapi.meta.user.service.IUserService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class JobCommonBusinessService implements IJobCommonBusinessService {
    private final IJobService iJobService;
    private final IUserService iUserService;
    private final IJobLogService iJobLogService;
    private final IJobCompleteService iJobCompleteService;
    private final IJobStopService iJobStopService;
    private final ICommonBusinessService iCommonBusinessService;

    public JobCommonBusinessService(IJobService iJobService,
                                    IUserService iUserService,
                                    IJobLogService iJobLogService,
                                    IJobCompleteService iJobCompleteService,
                                    ICommonBusinessService iCommonBusinessService, IJobStopService iJobStopService) {
        this.iJobService = iJobService;
        this.iUserService = iUserService;
        this.iJobLogService = iJobLogService;
        this.iJobCompleteService = iJobCompleteService;
        this.iCommonBusinessService = iCommonBusinessService;
        this.iJobStopService = iJobStopService;
    }


    /**
     * 统计我的所有任务未读信息
     * 包括所有任务的所有日志，完成，终止，申诉
     * @param in
     * @return
     * @throws Exception
     */
    @Override
    public Map totalMyUnread(Map in) throws Exception {
        /**
         * 按log, complete, stop, spotlight四个表来统计未读日志
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
        String jobId =  (String)in.get("jobId");

        UserInfo user = iCommonBusinessService.getUserByToken(token);

        Map out = new HashMap();
        Map qIn=new HashMap();

        qIn.put("jobId", jobId);
        qIn.put("userId", user.getUserId());

        //jobLog
        Integer total=iJobLogService.totalUnreadLog(qIn);
        out.put("totalUnreadLog", total);

        //jobComplete
        total=iJobCompleteService.totalUnreadComplete(qIn);
        out.put("totalUnreadComplete", total);

        //stop
        total=iJobStopService.totalUnreadStop(qIn);
        out.put("totalUnreadStop", total);

        //spotlight
        //todo

        return out;
    }

    @Override
    public Map totalMyLog(Map in) throws Exception {
        String token = in.get("token").toString();
        String jobId =  (String)in.get("jobId");

        UserInfo user = iCommonBusinessService.getUserByToken(token);

        Map out = new HashMap();
        Map qIn=new HashMap();

        qIn.put("jobId", jobId);
        //jobLog
        Integer total=iJobLogService.totalJobLog(qIn);
        out.put("totalJobLog", total);

        //jobComplete
        total=iJobCompleteService.totalJobComplete(qIn);
        out.put("totalComplete", total);

        //stop
        total=iJobStopService.totalStop(qIn);
        out.put("totalStop", total);

        //spotlight
        //todo

        return out;
    }

    @Override
    public Map getJobTinyByJobId(Map in) throws Exception {
        String jobId = in.get("jobId").toString();
        Job job = iJobService.getJobTinyByJobId(jobId);
        Map out = new HashMap();
        out.put("job", job);
        return out;
    }
}

package com.gogoyang.rpgapi.business.job.myJob.partyA.service;

import com.gogoyang.rpgapi.business.job.common.IJobCommonBusinessService;
import com.gogoyang.rpgapi.business.job.myJob.complete.service.ICompleteBusinessService;
import com.gogoyang.rpgapi.business.job.myJob.log.service.IMyLogBusinessService;
import com.gogoyang.rpgapi.business.job.myJob.stop.service.IStopBusinessService;
import com.gogoyang.rpgapi.framework.common.ICommonBusinessService;
import com.gogoyang.rpgapi.framework.constant.JobStatus;
import com.gogoyang.rpgapi.meta.job.entity.Job;
import com.gogoyang.rpgapi.meta.job.service.IJobService;
import com.gogoyang.rpgapi.meta.log.service.IJobLogService;
import com.gogoyang.rpgapi.meta.realname.service.IRealNameService;
import com.gogoyang.rpgapi.meta.user.entity.UserInfo;
import com.gogoyang.rpgapi.meta.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class PartyABusinessService implements IPartyABusinessService {
    private final IJobService iJobService;
    private final ICommonBusinessService iCommonBusinessService;
    private final IJobLogService iJobLogService;
    private final IJobCommonBusinessService iJobCommonBusinessService;

    @Autowired
    public PartyABusinessService(IJobService iJobService,
                                 ICommonBusinessService iCommonBusinessService,
                                 IJobLogService iJobLogService,
                                 IJobCommonBusinessService iJobCommonBusinessService) {
        this.iJobService = iJobService;
        this.iCommonBusinessService = iCommonBusinessService;
        this.iJobLogService = iJobLogService;
        this.iJobCommonBusinessService = iJobCommonBusinessService;
    }

    /**
     * 查询我是甲方的所有任务
     *
     * @param in
     * @return
     * @throws Exception
     */
    @Override
    public Map listMyPartyAJob(Map in) throws Exception {
        String token = in.get("token").toString();
        Integer pageIndex = (Integer) in.get("pageIndex");
        Integer pageSize = (Integer) in.get("pageSize");
        ArrayList statusList=(ArrayList)in.get("statusList");

        UserInfo user = iCommonBusinessService.getUserByToken(token);

        Map qIn=new HashMap();
        qIn.put("partyAId",user.getUserId());
        Integer offset=(pageIndex-1)* pageSize;
        qIn.put("offset", offset);
        qIn.put("size", pageSize);
        if(statusList.size()>0) {
            qIn.put("statusList", statusList);
        }
        Map jobsMap = iJobService.listPartyAJob(qIn);

        ArrayList<Job> jobs = null;
        if (jobsMap.get("jobs") != null) {
            jobs = (ArrayList<Job>) jobsMap.get("jobs");
            /**
             * 统计任务的日志
             */
            for (int i = 0; i < jobs.size(); i++) {
                Job job = jobs.get(i);
                qIn = new HashMap();
                qIn.put("jobId", jobs.get(i).getJobId());
                qIn.put("token", token);
                Map qOut = iJobCommonBusinessService.totalMyUnread(qIn);
                Integer totalUnreadLog = (Integer) qOut.get("totalUnreadLog");
                Integer totalUnreadComplete = (Integer) qOut.get("totalUnreadComplete");
                Integer totalUnreadStop = (Integer) qOut.get("totalUnreadStop");
                jobs.get(i).setTotalLogUnread(totalUnreadLog);
                jobs.get(i).setTotalCompleteUnread(totalUnreadComplete);
                jobs.get(i).setTotalStopUnread(totalUnreadStop);
            }
        }

        Map out = new HashMap();
        out.put("jobs", jobs);
        out.put("totalJobs", jobsMap.get("totalJobs"));

        return out;
    }

    /**
     * 甲方查询任务详情
     *
     * @param in
     * @return
     * @throws Exception
     */
    @Override
    public Map getPartyAJob(Map in) throws Exception {
        String token = in.get("token").toString();
        String jobId = in.get("jobId").toString();

        UserInfo user = iCommonBusinessService.getUserByToken(token);

        Job job = iJobService.getJobDetailByJobId(jobId);

        if (!job.getPartyAId().equals(user.getUserId())) {
            //当前登录用户不是甲方，没有查看任务权限
            throw new Exception("30003");
        }

        /**
         * 增加统计信息
         * 1、日志总数
         * 2、完成总数
         * 3、终止总数
         * 4、申诉总数
         */

        Map qIn = new HashMap();
        qIn.put("jobId", jobId);
        qIn.put("token", token);
        Map qOut = iJobCommonBusinessService.totalMyLog(qIn);
        Integer totalJobLog = (Integer) qOut.get("totalJobLog");
        job.setTotalLog(totalJobLog);
        Integer totalComplete = (Integer) qOut.get("totalComplete");
        job.setTotalComplete(totalComplete);
        Integer totalStop = (Integer) qOut.get("totalStop");
        job.setTotalStop(totalStop);

        Map out = new HashMap();
        out.put("job", job);

        return out;
    }
}

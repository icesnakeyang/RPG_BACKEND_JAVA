package com.gogoyang.rpgapi.business.job.myJob.partyB.service;

import com.gogoyang.rpgapi.business.job.common.IJobCommonBusinessService;
import com.gogoyang.rpgapi.framework.common.ICommonBusinessService;
import com.gogoyang.rpgapi.framework.constant.JobStatus;
import com.gogoyang.rpgapi.meta.job.entity.Job;
import com.gogoyang.rpgapi.meta.job.service.IJobService;
import com.gogoyang.rpgapi.meta.user.entity.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class PartyBBusinessService implements IPartyBBusinessService {
    private final IJobService iJobService;
    private final ICommonBusinessService iCommonBusinessService;
    private final IJobCommonBusinessService iJobCommonBusinessService;

    @Autowired
    public PartyBBusinessService(IJobService iJobService,
                                 ICommonBusinessService iCommonBusinessService,
                                 IJobCommonBusinessService iJobCommonBusinessService) {
        this.iJobService = iJobService;
        this.iCommonBusinessService = iCommonBusinessService;
        this.iJobCommonBusinessService = iJobCommonBusinessService;
    }

    /**
     * 查询所有我是乙方的任务
     *
     * @param in
     * @return
     * @throws Exception
     */
    @Override
    public Map listMyPartyBJob(Map in) throws Exception {
        String token = in.get("token").toString();
        Integer pageIndex = (Integer) in.get("pageIndex");
        Integer pageSize = (Integer) in.get("pageSize");
        ArrayList statusList = (ArrayList) in.get("statusList");
        String searchKey = (String) in.get("searchKey");

        UserInfo user = iCommonBusinessService.getUserByToken(token);

        Map qIn = new HashMap();
        qIn.put("partyBId", user.getUserId());
        if (statusList.size() > 0) {
            qIn.put("statusList", statusList);
        }
        Integer offset = (pageIndex - 1) * pageSize;
        qIn.put("offset", offset);
        qIn.put("size", pageSize);
        qIn.put("searchKey", searchKey);

        Map jobsMap = iJobService.listPartyBJob(qIn);

        ArrayList<Job> jobList = new ArrayList<>();

        if (jobsMap.get("jobs") != null) {
            jobList = (ArrayList<Job>) jobsMap.get("jobs");
            /**
             * 逐条统计未读日志数
             */
            for (int i = 0; i < jobList.size(); i++) {
                qIn = new HashMap();
                qIn.put("jobId", jobList.get(i).getJobId());
                qIn.put("token", token);
                Map qOut = iJobCommonBusinessService.totalMyUnread(qIn);
                Integer totalUnreadLog = (Integer) qOut.get("totalUnreadLog");
                Integer totalUnreadComplete = (Integer) qOut.get("totalUnreadComplete");
                Integer totalUnreadStop = (Integer) qOut.get("totalUnreadStop");
                jobList.get(i).setTotalLogUnread(totalUnreadLog);
                jobList.get(i).setTotalCompleteUnread(totalUnreadComplete);
                jobList.get(i).setTotalStopUnread(totalUnreadStop);
            }
        }
        Map out = new HashMap();
        out.put("jobList", jobList);
        out.put("totalJobs", jobsMap.get("totalJobs"));
        return out;
    }

    @Override
    public Map getPartyBJobDetail(Map in) throws Exception {
        String token = in.get("token").toString();
        String jobId = in.get("jobId").toString();

        UserInfo user = iCommonBusinessService.getUserByToken(token);

        Job job = iJobService.getJobDetailByJobId(jobId);

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

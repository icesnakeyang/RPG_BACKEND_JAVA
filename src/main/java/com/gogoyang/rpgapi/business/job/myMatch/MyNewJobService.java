package com.gogoyang.rpgapi.business.job.myMatch;

import com.gogoyang.rpgapi.framework.constant.JobStatus;
import com.gogoyang.rpgapi.framework.constant.LogStatus;
import com.gogoyang.rpgapi.meta.job.entity.Job;
import com.gogoyang.rpgapi.meta.job.service.IJobService;
import com.gogoyang.rpgapi.meta.apply.entity.JobApply;
import com.gogoyang.rpgapi.meta.apply.service.IJobApplyService;
import com.gogoyang.rpgapi.meta.match.entity.JobMatch;
import com.gogoyang.rpgapi.meta.match.service.IJobMatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

@Service
public class MyNewJobService implements IMyNewJobService{
    private final IJobMatchService iJobMatchService;
    private final IJobService iJobService;
    private final IJobApplyService iJobApplyService;

    @Autowired
    public MyNewJobService(IJobMatchService iJobMatchService, IJobService iJobService, IJobApplyService iJobApplyService) {
        this.iJobMatchService = iJobMatchService;
        this.iJobService = iJobService;
        this.iJobApplyService = iJobApplyService;
    }

    /**
     * 接受新任务
     * @param in
     * @throws Exception
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public void acceptNewJob(Map in) throws Exception {
        /**
         * 参数：jobId, userId
         * 修改jobMatch
         * 修改job
         * 修改其他用户的jobApply
         * 修改其他用户的jobMatch
         * 把任务金额转到乙方账户，乙方账户增加price
         */
        Integer jobId=(Integer)in.get("jobId");
        Integer userId=(Integer)in.get("userId");

        JobMatch jobMatch=iJobMatchService.loadJobMatchByUserIdAndJobId(userId, jobId);
        jobMatch.setProcessResult(LogStatus.ACCEPT);
        jobMatch.setProcessTime(new Date());
        iJobMatchService.updateJobMatch(jobMatch);

        Job job=iJobService.loadJobByJobIdTiny(jobId);
        job.setStatus(JobStatus.PROGRESS);
        job.setContractTime(new Date());
        job.setPartyBId(userId);
        job.setMatchId(jobMatch.getJobMatchId());
        iJobService.updateJob(job);

        ArrayList<JobApply> jobApplies=iJobApplyService.loadJobApplyByJobId(jobId);
        for(int i=0;i<jobApplies.size();i++){
            if(jobApplies.get(i).getApplyUserId()!=userId) {
                jobApplies.get(i).setProcessTime(new Date());
                jobApplies.get(i).setProcessResult(LogStatus.ACCEPT_BY_OTHERS);
                jobApplies.get(i).setProcessUserId(-1);
                iJobApplyService.updateJobApply(jobApplies.get(i));
            }
        }

        ArrayList<JobMatch> jobMatches=iJobMatchService.loadJobMatchByJobId(jobId);
        for(int i=0;i<jobMatches.size();i++){
            if(jobMatches.get(i).getMatchUserId()!=userId){
                jobMatches.get(i).setProcessTime(new Date());
                jobMatches.get(i).setProcessResult(LogStatus.ACCEPT_BY_OTHERS);
                iJobMatchService.updateJobMatch(jobMatches.get(i));
            }
        }
    }

    /**
     * 拒绝新任务
     * @param in
     * @throws Exception
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public void rejectNewJob(Map in) throws Exception {
        /**
         * 修改jobMatch
         *
         */
        Integer jobMatchId=(Integer)in.get("jobMatchId");
        String remark=in.get("remark").toString();
        JobMatch jobMatch=iJobMatchService.loadJobMatchByJobMatchId(jobMatchId);
        jobMatch.setProcessResult(LogStatus.REJECT);
        jobMatch.setProcessTime(new Date());
        jobMatch.setProcessRemark(remark);
        iJobMatchService.updateJobMatch(jobMatch);
    }
}
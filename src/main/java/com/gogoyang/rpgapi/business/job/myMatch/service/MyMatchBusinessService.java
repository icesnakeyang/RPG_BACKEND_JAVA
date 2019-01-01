package com.gogoyang.rpgapi.business.job.myMatch.service;

import com.gogoyang.rpgapi.framework.constant.AccountType;
import com.gogoyang.rpgapi.framework.constant.JobStatus;
import com.gogoyang.rpgapi.framework.constant.LogStatus;
import com.gogoyang.rpgapi.meta.account.entity.Account;
import com.gogoyang.rpgapi.meta.account.service.IAccountService;
import com.gogoyang.rpgapi.meta.apply.entity.JobApply;
import com.gogoyang.rpgapi.meta.apply.service.IJobApplyService;
import com.gogoyang.rpgapi.meta.job.entity.Job;
import com.gogoyang.rpgapi.meta.job.service.IJobService;
import com.gogoyang.rpgapi.meta.match.entity.JobMatch;
import com.gogoyang.rpgapi.meta.match.service.IJobMatchService;
import com.gogoyang.rpgapi.meta.user.entity.User;
import com.gogoyang.rpgapi.meta.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class MyMatchBusinessService implements IMyMatchBusinessService {
    private final IJobMatchService iJobMatchService;
    private final IJobService iJobService;
    private final IJobApplyService iJobApplyService;
    private final IAccountService iAccountService;
    private final IUserService iUserService;

    @Autowired
    public MyMatchBusinessService(IJobMatchService iJobMatchService,
                                  IJobService iJobService,
                                  IJobApplyService iJobApplyService,
                                  IAccountService iAccountService,
                                  IUserService iUserService) {
        this.iJobMatchService = iJobMatchService;
        this.iJobService = iJobService;
        this.iJobApplyService = iJobApplyService;
        this.iAccountService = iAccountService;
        this.iUserService = iUserService;
    }

    /**
     * 读取所有RPG秘书分配给我的新任务
     *
     * @param in
     * @return
     * @throws Exception
     */
    @Override
    public Map loadJobMatchToMe(Map in) throws Exception {
        /**
         * 参数：token
         * 1、根据token读取userInfo
         * 2、根据userId读取所有jobMatch
         * 3、逐条根据jobMatch读取所有job
         */
        String token = in.get("token").toString();
        User user = iUserService.getUserByToken(token);
        if (user == null) {
            throw new Exception("10004");
        }

        ArrayList<JobMatch> newMatchs = iJobMatchService.loadMyNewJobMatch(user.getUserId());
        ArrayList newJobs = new ArrayList();
        for (int i = 0; i < newMatchs.size(); i++) {
            Map map = new HashMap();
            Job job = iJobService.getJobByJobIdTiny(newMatchs.get(i).getJobId());
            if (job != null) {
                map.put("match", newMatchs.get(i));
                User partyAUser=iUserService.getUserByUserId(job.getPartyAId());
                if(partyAUser.getRealName()!=null) {
                    job.setPartyAName(partyAUser.getRealName());
                }else{
                    job.setPartyAName(partyAUser.getEmail());
                }
                job.setJobApplyNum(iJobApplyService.countApplyUsers(job.getJobId()));
                job.setJobMatchNum(iJobMatchService.countMatchingUsers(job.getJobId()));
                map.put("job", job);
                newJobs.add(map);
            }
        }
        Map out = new HashMap();
        out.put("newJobs", newJobs);
        return out;
    }

    /**
     * 接受新任务
     *
     * @param in
     * @throws Exception
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public void acceptNewJob(Map in) throws Exception {
        /**
         * 参数：jobId, token
         * 修改jobMatch
         * 修改job
         * 修改其他用户的jobApply
         * 修改其他用户的jobMatch
         * 把任务金额转到乙方账户，乙方账户增加price
         */
        Integer jobId = (Integer) in.get("jobId");
        String token = in.get("token").toString();
        User user = iUserService.getUserByToken(token);
        if (user == null) {
            throw new Exception("10004");
        }

        Integer userId = user.getUserId();

        //把jobMatch改成Accept
        JobMatch jobMatch = iJobMatchService.loadJobMatchByUserIdAndJobId(userId, jobId);
        jobMatch.setProcessResult(LogStatus.ACCEPT);
        jobMatch.setProcessTime(new Date());
        iJobMatchService.updateJobMatch(jobMatch);

        //把job的状态修改为progress
        Job job = iJobService.getJobByJobIdTiny(jobId);
        job.setStatus(JobStatus.PROGRESS);
        job.setContractTime(new Date());
        job.setPartyBId(userId);
        job.setMatchId(jobMatch.getJobMatchId());
        iJobService.updateJob(job);

        //查找所有其他申请了该任务的用户，把申请结果处理为accept_by_others
        ArrayList<JobApply> jobApplies = iJobApplyService.listJobApplyByJobId(jobId);
        for (int i = 0; i < jobApplies.size(); i++) {
            if (jobApplies.get(i).getApplyUserId() != userId) {
                jobApplies.get(i).setProcessTime(new Date());
                jobApplies.get(i).setProcessResult(LogStatus.ACCEPT_BY_OTHERS);
                jobApplies.get(i).setProcessUserId(-1);
                iJobApplyService.updateJobApply(jobApplies.get(i));
            }
        }

        //查找所有分配了该任务的其他用户，把处理结果修改为accept_by_others
        ArrayList<JobMatch> jobMatches = iJobMatchService.loadJobMatchByJobId(jobId);
        for (int i = 0; i < jobMatches.size(); i++) {
            if (jobMatches.get(i).getMatchUserId() != userId) {
                jobMatches.get(i).setProcessTime(new Date());
                jobMatches.get(i).setProcessResult(LogStatus.ACCEPT_BY_OTHERS);
                iJobMatchService.updateJobMatch(jobMatches.get(i));
            }
        }

        //给乙方账户增加任务金额
        Account account = new Account();
        account.setAmount(job.getPrice());
        account.setCreatedTime(new Date());
        account.setType(AccountType.ACCEPT);
        account.setUserId(userId);
        iAccountService.insertNewAccount(account);

        //刷新乙方的balance
        Map money = iAccountService.loadAccountBalance(userId);
        user.setAccount((Double) money.get("balance"));
        iUserService.update(user);
    }

    /**
     * 拒绝新任务
     *
     * @param in
     * @throws Exception
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public void rejectNewJob(Map in) throws Exception {
        /**
         * 参数：jobId, token, remark
         * 修改jobMatch
         *
         */
        String token=in.get("token").toString();
        Integer jobId=(Integer)in.get("jobId");
        String remark=in.get("remark").toString();

        User user=iUserService.getUserByToken(token);
        JobMatch jobMatch=iJobMatchService.loadJobMatchByUserIdAndJobId(user.getUserId(), jobId);
        jobMatch.setProcessResult(LogStatus.REJECT);
        jobMatch.setProcessTime(new Date());
        jobMatch.setProcessRemark(remark);
        iJobMatchService.updateJobMatch(jobMatch);
    }
}

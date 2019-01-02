package com.gogoyang.rpgapi.business.job.common.service;

import com.gogoyang.rpgapi.framework.constant.AccountType;
import com.gogoyang.rpgapi.framework.constant.JobStatus;
import com.gogoyang.rpgapi.meta.account.entity.Account;
import com.gogoyang.rpgapi.meta.account.service.IAccountService;
import com.gogoyang.rpgapi.meta.apply.service.IJobApplyService;
import com.gogoyang.rpgapi.meta.job.entity.Job;
import com.gogoyang.rpgapi.meta.job.service.IJobService;
import com.gogoyang.rpgapi.meta.user.entity.User;
import com.gogoyang.rpgapi.meta.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JobCommonBusinessService implements IJobCommonBusinessService{
    private final IUserService iUserService;
    private final IJobService iJobService;
    private final IAccountService iAccountService;
    private final IJobApplyService iJobApplyService;

    @Autowired
    public JobCommonBusinessService(IUserService iUserService,
                                    IJobService iJobService,
                                    IAccountService iAccountService,
                                    IJobApplyService iJobApplyService) {
        this.iUserService = iUserService;
        this.iJobService = iJobService;
        this.iAccountService = iAccountService;
        this.iJobApplyService = iJobApplyService;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void publishNewJob(Map in) throws Exception {
        /**
         * 检查任务是否已经发布了
         * create a common
         * minus price account
         * throw 10043 if user has not enough balance
         * refresh account of common
         */
        Integer taskId=(Integer)in.get("taskId");

        //读取当前用户
        User user=iUserService.getUserByToken(in.get("token").toString());
        if(user==null){
            //当前用户不存在
            throw new Exception("10004");
        }

        //根据taskId读取job
        Job job=iJobService.getJobByTaskId(taskId);
        if(job!=null){
            //该task已经发布过了，且，发布的任务正在进行中
            if(job.getStatus()== JobStatus.PROGRESS) {
                throw new Exception("10092");
            }
            //该Task已经发布过了在，且，目前该任务正在匹配中
            if(job.getStatus()==JobStatus.MATCHING){
                throw new Exception("10092");
            }
            //该Task已经发布过了，且，正在等待匹配中
            if(job.getStatus()==JobStatus.PENDING){
                throw new Exception("10092");
            }
        }

        //创建Job
        job=new Job();
        job.setPartyAId(user.getUserId());
        job.setCreatedTime(new Date());
        job.setCode(in.get("code").toString());
        job.setDays((Integer)in.get("days"));
        job.setPrice((Double)in.get("price"));
        job.setStatus(JobStatus.PENDING);
        job.setTaskId((Integer)in.get("taskId"));
        job.setTitle(in.get("title").toString());
        job.setDetail(in.get("detail").toString());
        job=iJobService.insertJob(job);

        //新增account记录，publish任务时在甲方账户扣除对应的任务金额
        Account account=new Account();
        account.setUserId(job.getPartyAId());
        account.setAmount(job.getPrice());
        account.setCreatedTime(new Date());
        account.setType(AccountType.PUBLISH);
        iAccountService.insertNewAccount(account);

        //计算甲方账户的balance余额
        Map accountMap=iAccountService.loadAccountBalance(job.getPartyAId());
        Double balance=(Double)accountMap.get("balance");
        Double income=(Double)accountMap.get("income");
        Double outgoing=(Double)accountMap.get("outgoing");

        //更新甲方的账户统计信息
        user.setAccount(balance);
        user.setAccountIn(income);
        user.setAccountOut(outgoing);
        iUserService.update(user);

    }

    @Override
    public Map listPublicJob(Map in) throws Exception {
        Integer pageIndex=(Integer)in.get("pageIndex");
        Integer pageSize=(Integer)in.get("pageSize");
        Map qIn=new HashMap();
        qIn.put("pageIndex", pageIndex);
        qIn.put("pageSize", pageSize);
        Page<Job> jobPage= iJobService.listPublicJob(qIn);
        for(int i=0;i<jobPage.getContent().size();i++){
            Job job=jobPage.getContent().get(i);
            User user=iUserService.getUserByUserId(job.getPartyAId());
            if(user.getRealName()!=null){
                jobPage.getContent().get(i).setPartyAName(user.getRealName());
            }else{
                jobPage.getContent().get(i).setPartyAName(user.getEmail());
            }
        }
        Map out=new HashMap();
        out.put("jobs", jobPage);
        return out;
    }

    @Override
    public Map getJobDetail(Map in) throws Exception {
        Integer jobId=(Integer)in.get("jobId");
        Job job = iJobService.getJobByJobId(jobId);
        User partyA=iUserService.getUserByUserId(job.getPartyAId());
        if(partyA.getRealName()!=null) {
            job.setPartyAName(partyA.getRealName());
        }else{
            job.setPartyAName(partyA.getEmail());
        }
        job.setJobApplyNum(iJobApplyService.countApplyUsers(job.getJobId()));
        Map out=new HashMap();
        out.put("job", job);
        return out;
    }

    @Override
    public Map getJobTiny(Map in) throws Exception {
        Integer jobId=(Integer)in.get("jobId");
        Job job=iJobService.getJobByJobIdTiny(jobId);
        Map out=new HashMap();
        out.put("job", job);
        return out;
    }
}

package com.gogoyang.rpgapi.job.business.publish;

import com.gogoyang.rpgapi.account.entity.Account;
import com.gogoyang.rpgapi.account.service.IAccountService;
import com.gogoyang.rpgapi.constant.AccountType;
import com.gogoyang.rpgapi.constant.JobStatus;
import com.gogoyang.rpgapi.job.meta.job.entity.Job;
import com.gogoyang.rpgapi.job.meta.job.entity.JobDetail;
import com.gogoyang.rpgapi.job.meta.job.service.IJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Map;

@Service
public class PublishJobService implements IPublishJobService{
    private final IJobService iJobService;
    private final IAccountService iAccountService;

    @Autowired
    public PublishJobService(IJobService iJobService, IAccountService iAccountService) {
        this.iJobService = iJobService;
        this.iAccountService = iAccountService;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public Job publishJob(Map in) throws Exception {
        /**
         * create a job
         * minus price account
         * throw 10043 if user has not enough balance
         * refresh account of job
         */
        Job job=new Job();
        job.setPartyAId((Integer)in.get("userId"));
        job.setCreatedTime(new Date());
        job.setCode(in.get("code").toString());
        job.setDays((Integer)in.get("days"));
        job.setPrice((Double)in.get("price"));
        job.setStatus(JobStatus.MATCHING);
        job.setTaskId((Integer)in.get("taskId"));
        job.setTitle(in.get("title").toString());
        job.setDetail(in.get("detail").toString());
        job=iJobService.insertJob(job);

        Account account=new Account();
        account.setUserId(job.getPartyAId());
        account.setAmount(job.getPrice());
        account.setCreatedTime(new Date());
        account.setType(AccountType.PUBLISH);
        iAccountService.insertNewAccount(account);

        job.setAccountBalance((Double)iAccountService.refreshAccountBalance(job.getPartyAId()).get("balance"));
        iJobService.updateJob(job);

        return job;
    }
}

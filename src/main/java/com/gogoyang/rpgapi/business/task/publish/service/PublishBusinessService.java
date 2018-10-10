package com.gogoyang.rpgapi.business.task.publish.service;

import com.gogoyang.rpgapi.meta.account.entity.Account;
import com.gogoyang.rpgapi.meta.account.service.IAccountService;
import com.gogoyang.rpgapi.framework.constant.AccountType;
import com.gogoyang.rpgapi.framework.constant.JobStatus;
import com.gogoyang.rpgapi.meta.job.entity.Job;
import com.gogoyang.rpgapi.meta.job.service.IJobService;
import com.gogoyang.rpgapi.meta.user.userInfo.entity.UserInfo;
import com.gogoyang.rpgapi.meta.user.userInfo.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class PublishBusinessService implements IPublishBusinessService{
    private final IJobService iJobService;
    private final IAccountService iAccountService;
    private final IUserInfoService iUserInfoService;

    @Autowired
    public PublishBusinessService(IJobService iJobService, IAccountService iAccountService, IUserInfoService iUserInfoService) {
        this.iJobService = iJobService;
        this.iAccountService = iAccountService;
        this.iUserInfoService = iUserInfoService;
    }

    /**
     * 发布一个新任务
     * publish a new job
     * @param in
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public Map publishJob(Map in) throws Exception {
        /**
         * create a job
         * minus price account
         * throw 10043 if user has not enough balance
         * refresh account of job
         */
        UserInfo userInfo=iUserInfoService.loadUserByToken(in.get("token").toString());
        if(userInfo==null){
            throw new Exception("10004");
        }
        Job job=new Job();
        job.setPartyAId(userInfo.getUserId());
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

        Double balance;
        Map balance1=iAccountService.refreshAccountBalance(job.getPartyAId());
        balance=(Double)balance1.get("balance");
        job.setAccountBalance((Double)iAccountService.refreshAccountBalance(job.getPartyAId()).get("balance"));
        iJobService.updateJob(job);

        Map out=new HashMap();
        out.put("job", job);
        return out;
    }
}

package com.gogoyang.rpgapi.business.task.publish.service;

import com.gogoyang.rpgapi.meta.account.entity.Account;
import com.gogoyang.rpgapi.meta.account.service.IAccountService;
import com.gogoyang.rpgapi.framework.constant.AccountType;
import com.gogoyang.rpgapi.framework.constant.JobStatus;
import com.gogoyang.rpgapi.meta.job.entity.Job;
import com.gogoyang.rpgapi.meta.job.service.IJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Map;

@Service
public class PublishBusinessService implements IPublishBusinessService{
    private final IJobService iJobService;
    private final IAccountService iAccountService;

    @Autowired
    public PublishBusinessService(IJobService iJobService,
                                  IAccountService iAccountService) {
        this.iJobService = iJobService;
        this.iAccountService = iAccountService;
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
    public void publishJob(Map in) throws Exception {
        /**
         * 检查任务是否已经发布了
         * create a job
         * minus price account
         * throw 10043 if user has not enough balance
         * refresh account of job
         */
        Integer taskId=(Integer)in.get("taskId");

        //读取当前用户
        UserInfo userInfo=iUserInfoService.getUserByToken(in.get("token").toString());
        if(userInfo==null){
            //当前用户不存在
            throw new Exception("10004");
        }

        //根据taskId读取job
        Job job=iJobService.getJobByTaskId(taskId);
        if(job!=null){
            //该task已经发布过了，且，发布的任务正在进行中
            if(job.getStatus()==JobStatus.PROGRESS) {
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
        job.setPartyAId(userInfo.getUserId());
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
        userInfo.setAccount(balance);
        userInfo.setAccountIn(income);
        userInfo.setAccountOut(outgoing);
        iUserInfoService.updateUser(userInfo);
    }
}

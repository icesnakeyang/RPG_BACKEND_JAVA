package com.gogoyang.rpgapi.business.job.myJob.stop.service;

import com.gogoyang.rpgapi.framework.common.GogoTools;
import com.gogoyang.rpgapi.framework.common.ICommonBusinessService;
import com.gogoyang.rpgapi.framework.constant.AccountType;
import com.gogoyang.rpgapi.framework.constant.JobStatus;
import com.gogoyang.rpgapi.framework.constant.LogStatus;
import com.gogoyang.rpgapi.meta.account.entity.Account;
import com.gogoyang.rpgapi.meta.account.service.IAccountService;
import com.gogoyang.rpgapi.meta.job.entity.Job;
import com.gogoyang.rpgapi.meta.job.service.IJobService;
import com.gogoyang.rpgapi.meta.stop.entity.JobStop;
import com.gogoyang.rpgapi.meta.stop.service.IJobStopService;
import com.gogoyang.rpgapi.meta.user.entity.UserInfo;
import com.gogoyang.rpgapi.meta.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class StopBusinessService implements IStopBusinessService {
    private final IJobStopService iJobStopService;
    private final IJobService iJobService;
    private final IUserService iUserService;
    private final ICommonBusinessService iCommonBusinessService;
    private final IAccountService iAccountService;

    @Autowired
    public StopBusinessService(IJobStopService iJobStopService,
                               IJobService iJobService,
                               IUserService iUserService,
                               ICommonBusinessService iCommonBusinessService,
                               IAccountService iAccountService) {
        this.iJobStopService = iJobStopService;
        this.iJobService = iJobService;
        this.iUserService = iUserService;
        this.iCommonBusinessService = iCommonBusinessService;
        this.iAccountService = iAccountService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createJobStop(Map in) throws Exception {
        /**
         * 甲乙双方同时只能处理一件终止申请，只要有终止未处理，就不能再次创建终止申请
         */
        String jobId =  in.get("jobId").toString();
        JobStop jobStop = iJobStopService.loadJobStopUnProcess(jobId);
        if (jobStop != null) {
            throw new Exception("10069");
        }

        String token = in.get("token").toString();
        UserInfo user = iUserService.getUserByToken(token);
        if (user == null) {
            throw new Exception("10004");
        }
        String content=in.get("content").toString();
        Double refund=(Double)in.get("refund");

        jobStop = new JobStop();
        jobStop.setContent(content);
        jobStop.setCreatedTime(new Date());
        jobStop.setCreatedUserId(user.getUserId());
        jobStop.setJobId(jobId);
        jobStop.setRefund(refund);
        iJobStopService.insertJobStop(jobStop);
    }

    @Override
    public Map loadStopList(Map in) throws Exception {
        String jobId =  in.get("jobId").toString();
        Integer pageIndex = (Integer) in.get("pageIndex");
        Integer pageSize = (Integer) in.get("pageSize");
        ArrayList<JobStop> stops = iJobStopService.loadJobStopByJobId(jobId, pageIndex, pageSize);

        Map out=new HashMap();
        out.put("stops", stops);
        return out;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setStopReadTime(Map in) throws Exception {
        String jobId = in.get("jobId").toString();
        String token = in.get("token").toString();
        UserInfo user = iCommonBusinessService.getUserByToken(token);

        ArrayList<JobStop> stops = iJobStopService.loadMyUnReadStop(jobId, user.getUserId());

        for (int i = 0; i < stops.size(); i++) {
            stops.get(i).setReadTime(new Date());
            iJobStopService.updateJobStop(stops.get(i));
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rejectStop(Map in) throws Exception {
        String token = in.get("token").toString();
        String jobId =  in.get("jobId").toString();
        String processRemark=(String)in.get("processRemark");

        /**
         * 实际上，终止任务是在甲乙双方线上线下提前沟通多次后达成的一致，终止仅仅是对系统管理的一个操作，并没有太大的实际意义。
         * 如果拒绝终止，把本次终止申请处理了就行了，不用上纲上线。
         * 甲乙双方只能同时处理一件终止申请，不能没完没了，也就是说，只要双方还有一条终止申请没有被处理，就不能再次提出终止。
         */

        Job job = iJobService.getJobTinyByJobId(jobId);
        if (!job.getStatus().equals(JobStatus.PROGRESS)) {
            //只有progress状态的任务才能操作终止
            throw new Exception("10070");
        }

        UserInfo user = iCommonBusinessService.getUserByToken(token);

        JobStop jobStop = iJobStopService.loadJobStopUnProcess(jobId);

        jobStop.setProcessRemark(processRemark);
        jobStop.setProcessTime(new Date());
        jobStop.setProcessUserId(user.getUserId());
        jobStop.setStatus(LogStatus.REJECT.toString());
        iJobStopService.updateJobStop(jobStop);
    }

    /**
     * 接受终止任务
     * @param in
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void acceptStop(Map in) throws Exception {
        /**
         * 1、根据token读取当前用户
         * 2、检查jobId是否progress
         * 3、检查是否自己创建的stop
         * 4、把stop处理为accept
         * 5、把job改为stopped
         * 6、甲方account增加refund
         * 7、乙方account减少refund
         * 8、甲方honor扣除refund
         * 9、乙方honor扣除refund
         */

        String token=in.get("token").toString();
        String jobId=in.get("jobId").toString();
        String processRemark=(String)in.get("processRemark");

        UserInfo user = iCommonBusinessService.getUserByToken(token);

        //检查任务是否正在进行
        Job job=iJobService.getJobTinyByJobId(jobId);
        if(!job.getStatus().equals(JobStatus.PROGRESS)){
            throw new Exception("10071");
        }

        //是否自己创建的stop
        JobStop jobStop=iJobStopService.loadJobStopUnProcess(jobId);
        if(jobStop.getCreatedUserId().equals(user.getUserId())){
            throw new Exception("10074");
        }

        //任务状态终止
        Job jobUpdate=new Job();
        jobUpdate.setJobId(jobId);
        jobUpdate.setStatus(JobStatus.STOPPED.toString());
        iJobService.updateJob(job);

        //保存终止日志
        JobStop jobStopUpdate=new JobStop();
        jobStopUpdate.setStopId(jobStop.getStopId());
        jobStopUpdate.setStatus(LogStatus.ACCEPT.toString());
        jobStopUpdate.setProcessUserId(user.getUserId());
        jobStopUpdate.setProcessTime(new Date());
        jobStopUpdate.setProcessRemark(processRemark);
        iJobStopService.updateJobStop(jobStopUpdate);

        //处理refund
        Double refund = jobStop.getRefund();
        if(refund>0) {
            UserInfo userA = iUserService.getUserByUserId(job.getPartyAId());

            //一般来说，退款都是从乙方账户退还到甲方账户
            /**
             * 把refund金额增加到甲方账户
             */
            Account account=new Account();
            account.setAccountId(GogoTools.UUID());
            account.setAmount(refund);
            account.setCreatedTime(new Date());
            account.setJobId(jobId);
            account.setRemark(processRemark);
            account.setType(AccountType.REFUND_IN.toString());
            account.setUserId(job.getPartyAId());
            iAccountService.createAccount(account);

            /**
             * 扣除乙方账户
             */
            account=new Account();
            account.setAccountId(GogoTools.UUID());
            account.setAmount(refund);
            account.setCreatedTime(new Date());
            account.setJobId(jobId);
            account.setRemark(processRemark);
            account.setType(AccountType.REFUND_OUT.toString());
            account.setUserId(job.getPartyBId());
            iAccountService.createAccount(account);
        }
    }
}

package com.gogoyang.rpgapi.business.admin.secretary.match.service;

import com.gogoyang.rpgapi.framework.constant.AccountType;
import com.gogoyang.rpgapi.framework.constant.JobStatus;
import com.gogoyang.rpgapi.framework.constant.LogStatus;
import com.gogoyang.rpgapi.framework.constant.RoleType;
import com.gogoyang.rpgapi.meta.account.entity.Account;
import com.gogoyang.rpgapi.meta.account.service.IAccountService;
import com.gogoyang.rpgapi.meta.admin.entity.Admin;
import com.gogoyang.rpgapi.meta.admin.service.IAdminService;
import com.gogoyang.rpgapi.meta.apply.entity.JobApply;
import com.gogoyang.rpgapi.meta.apply.service.IJobApplyService;
import com.gogoyang.rpgapi.meta.job.entity.Job;
import com.gogoyang.rpgapi.meta.user.entity.User;
import com.gogoyang.rpgapi.meta.user.service.IUserService;
import org.apache.tomcat.util.digester.ArrayStack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class SecretaryMatchBusinessService implements ISecretaryMatchBusinessService {
    private final IAdminService iAdminService;
    private final IJobService iJobService;
    private final IJobApplyService iJobApplyService;
    private final IAccountService iAccountService;
    private final IUserService iUserService;

    @Autowired
    public SecretaryMatchBusinessService(IAdminService iAdminService,
                                         IJobService iJobService,
                                         IJobApplyService iJobApplyService,
                                         IAccountService iAccountService,
                                         IUserService iUserService) {
        this.iAdminService = iAdminService;
        this.iJobService = iJobService;
        this.iJobApplyService = iJobApplyService;
        this.iAccountService = iAccountService;
        this.iUserService = iUserService;
    }



    /**
     * list new applies
     * @param in
     * @return
     * @throws Exception
     */
    @Override
    public Map listAppliedJob(Map in) throws Exception {
        /**
         * list job status=matching
         */
        String token=in.get("token").toString();
        Integer pageIndex=(Integer)in.get("pageIndex");
        Integer pageSize=(Integer)in.get("pageSize");

        Admin admin=iAdminService.getAdminByToken(token);
        if(admin==null){
            throw new Exception("10004");
        }

        /**
         * 查询出正在等待匹配的任务
         */
        Page<Job> jobs=iJobService.listJobByStatus(JobStatus.MATCHING, pageIndex, pageSize);

        ArrayList<Map<String, Object>> list=new ArrayStack<Map<String, Object>>();
        for(int i=0;i<jobs.getContent().size();i++){
            Job job=jobs.getContent().get(i);
            Map map=new HashMap();
            map.put("title", job.getTitle());
            map.put("code", job.getCode());
            map.put("price", job.getPrice());
            map.put("days", job.getDays());
            map.put("createdTime", job.getCreatedTime());
            map.put("jobApplyNum", job.getJobApplyNum());
            map.put("partyAId", job.getPartyAId());
            map.put("jobId", job.getJobId());
            list.add(map);
        }

        Map out=new HashMap();
        out.put("newApplyList", list);
        return out;
    }

    @Override
    public Map listApplyByJobId(Map in) throws Exception {
        String token = in.get("token").toString();
        Integer jobId = (Integer) in.get("jobId");
        Integer pageIndex=(Integer)in.get("pageIndex");
        Integer pageSize=(Integer)in.get("pageSize");

        /**
         * 当前操作用户必须是RPG秘书权限
         */
        Admin admin = iAdminService.getAdminByToken(token);
        if (admin == null) {
            throw new Exception("10004");
        }
        if (admin.getRoleType() != RoleType.SECRETARY) {
            throw new Exception("10040");
        }

        /**
         * read jobApply by jobId where processResult==null
         */
        ArrayList<JobApply> jobApplies = iJobApplyService.listJobApplyByJobId(jobId);

        ArrayList<Map<String, Object>> applyList=new ArrayList<Map<String, Object>>();
        for(int i=0;i<jobApplies.size();i++){
            JobApply apply=jobApplies.get(i);
            Map map=new HashMap();
            User user=iUserService.getUserByUserId(apply.getApplyUserId());
            if(user.getRealName()!=null){
                map.put("applyUser", user.getRealName());
            }else {
                map.put("applyUser", user.getEmail());
            }
            map.put("applyUserId", user.getUserId());
            map.put("applyTime", apply.getApplyTime());
            map.put("applyId", apply.getJobApplyId());
            map.put("content", apply.getContent());
            applyList.add(map);
        }

        Map out = new HashMap();
        out.put("applyList", applyList);
        return out;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void agreeApply(Map in) throws Exception {
        /**
         * check authority
         * load apply
         * set to agree
         * set common to progress
         * transfer money to user
         * set all other apply to disable
         */
        String token=in.get("token").toString();
        Integer applyId=(Integer)in.get("applyId");

        //检查用户权限，必须是秘书
        Admin admin=iAdminService.getAdminByToken(token);
        if(admin==null){
            throw new Exception("10004");
        }
        if(admin.getRoleType()!=RoleType.SECRETARY){
            throw new Exception("10034");
        }

        //读取申请，保存为成功
        JobApply jobApply=iJobApplyService.getJobApplyByJobApplyId(applyId);
        if(jobApply==null){
            throw new Exception("10109");
        }
        jobApply.setProcessUserId(admin.getAdminId());
        jobApply.setProcessTime(new Date());
        jobApply.setProcessResult(LogStatus.ACCEPT);
        iJobApplyService.updateJobApply(jobApply);

        //读取任务，保存乙方信息，任务状态为进行中
        Job job=iJobService.getJobByJobIdTiny(jobApply.getJobId());
        if(job==null){
            throw new Exception("10112");
        }
        job.setStatus(JobStatus.PROGRESS);
        job.setContractTime(jobApply.getProcessTime());
        job.setPartyBId(jobApply.getApplyUserId());
        iJobService.updateJob(job);

        //把任务金额转给乙方
        User userB=iUserService.getUserByUserId(jobApply.getApplyUserId());
        if(userB==null){
            throw new Exception("10019");
        }
        Account account=new Account();
        account.setAmount(job.getPrice());
        account.setCreatedTime(jobApply.getProcessTime());
        account.setType(AccountType.ACCEPT);
        account.setUserId(jobApply.getApplyUserId());
        account.setJobId(jobApply.getJobId());
        iAccountService.insertNewAccount(account);

        Map accountMap=new HashMap();
        accountMap=iAccountService.loadAccountBalance(jobApply.getApplyUserId());
        Double balance=(Double)accountMap.get("balance");
        Double income=(Double)accountMap.get("income");
        Double outgoing=(Double)accountMap.get("outgoing");

        //更新乙方的账户统计信息
        userB.setAccount(balance);
        userB.setAccountIn(income);
        userB.setAccountOut(outgoing);
        iUserService.update(userB);

        //处理其他用户的申请
        ArrayList<JobApply> otherApplies=iJobApplyService.listJobApplyByNotProcesJobId(jobApply.getJobId());
        for(int i=0;i<otherApplies.size();i++){
            JobApply otherApply=otherApplies.get(i);
            otherApply.setProcessResult(LogStatus.ACCEPT_BY_OTHERS);
            otherApply.setProcessTime(jobApply.getProcessTime());
            otherApply.setProcessUserId(admin.getAdminId());
            iJobApplyService.updateJobApply(otherApply);
        }
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void rejectApply(Map in) throws Exception {
        /**
         * check authority
         * load apply
         * set to reject
         */
        String token=in.get("token").toString();
        Integer applyId=(Integer)in.get("applyId");
        String remark=(String)in.get("remark");

        Admin admin=iAdminService.getAdminByToken(token);
        if(admin==null){
            throw new Exception("10004");
        }

        if(admin.getRoleType()!=RoleType.SECRETARY){
            throw new Exception("10034");
        }

        JobApply jobApply=iJobApplyService.getJobApplyByJobApplyId(applyId);
        if(jobApply==null){
            throw new Exception("10109");
        }

        jobApply.setProcessResult(LogStatus.REJECT);
        jobApply.setProcessTime(new Date());
        jobApply.setProcessUserId(admin.getAdminId());
        jobApply.setProcessRemark(remark);

        iJobApplyService.updateJobApply(jobApply);
    }

    @Override
    public Map getApplyJobTiny(Map in) throws Exception {
        String token=in.get("token").toString();
        Integer jobId=(Integer)in.get("jobId");

        Admin admin=iAdminService.getAdminByToken(token);
        if(admin==null){
            throw new Exception("10004");
        }

        Job job=iJobService.getJobByJobIdTiny(jobId);

        Map out=new HashMap();
        out.put("job", job);

        return out;
    }

    @Override
    public Map getApplyJobDetail(Map in) throws Exception {
        String token=in.get("token").toString();
        Integer jobId=(Integer)in.get("jobId");

        User user=iUserService.getUserByToken(token);
        if(user==null){
            throw new Exception("10004");
        }

        Job job=iJobService.getJobByJobId(jobId);

        Map out=new HashMap();
        out.put("job", job);

        return out;
    }

    /**
     * read one users apply history
     * @param in
     * @return
     * @throws Exception
     */
    @Override
    public Map listApplyHistory(Map in) throws Exception {
        /**
         * input: pageIndex, pageSize, userId
         * output:
         *      createdTime: if contracted then contractTime, else createdTime of jobApply
         *      jobTitle: common.title
         *      jobPrice: common.price
         *      status: if contracted common status, else apply status
         *      spotNum: common.spotnum
         */
        String token=in.get("token").toString();
        Integer pageIndex=(Integer)in.get("pageIndex");
        Integer pageSize=(Integer)in.get("pageSize");
        Integer userId=(Integer)in.get("userId");

        Admin admin=iAdminService.getAdminByToken(token);
        if(admin==null){
            throw new Exception("10004");
        }

        Page<JobApply> jobApplies=iJobApplyService.listJobapplybyUserId(userId, pageIndex, pageSize);

        ArrayList<Map<String, Object>> list=new ArrayStack<Map<String, Object>>();
        for(int i=0;i<jobApplies.getContent().size();i++){
            JobApply jobApply=jobApplies.getContent().get(i);
            Map map=new HashMap();
            map.put("jobId", jobApply.getJobId());
            map.put("applyId", jobApply.getJobApplyId());
            map.put("applyUserId", jobApply.getApplyUserId());
            User user=iUserService.getUserByUserId(jobApply.getApplyUserId());
            if(user.getRealName()!=null) {
                map.put("username", user.getRealName());
            }else {
                map.put("username", user.getEmail());
            }
            map.put("applyTime", jobApply.getApplyTime());
            map.put("processUserId", jobApply.getProcessUserId());
            map.put("processResult", jobApply.getProcessResult());
            map.put("processTime", jobApply.getProcessTime());
            Job job=iJobService.getJobByJobIdTiny(jobApply.getJobId());
            map.put("title", job.getTitle());
            map.put("price", job.getPrice());
            map.put("jobStatus",job.getStatus());
            list.add(map);
        }


        Map out=new HashMap();
        out.put("jobApply", list);
        return out;
    }

    @Override
    public Map getApplyDetail(Map in) throws Exception {
        String token=in.get("token").toString();
        Integer applyId=(Integer)in.get("applyId");

        Admin admin=iAdminService.getAdminByToken(token);
        if(admin==null){
            throw new Exception("10004");
        }

        JobApply jobApply=iJobApplyService.getJobApplyByApplyId(applyId);

        Job job=iJobService.getJobByJobIdTiny(jobApply.getJobId());
        User user=iUserService.getUserByUserId(jobApply.getApplyUserId());

        Map out=new HashMap();
        out.put("title", job.getTitle());
        out.put("applyTime", jobApply.getApplyTime());
        out.put("applyUser", user.getRealName());
        out.put("content", jobApply.getContent());
        out.put("processResult", jobApply.getProcessResult());
        out.put("processRemark", jobApply.getProcessRemark());
        out.put("processUserId", jobApply.getProcessUserId());
        out.put("processTime", jobApply.getProcessTime());

        return out;
    }
}

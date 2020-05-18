package com.gogoyang.rpgapi.business.admin.secretary.match.service;

import com.gogoyang.rpgapi.framework.common.GogoTools;
import com.gogoyang.rpgapi.framework.common.ICommonBusinessService;
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
import com.gogoyang.rpgapi.meta.job.service.IJobService;
import com.gogoyang.rpgapi.meta.user.entity.UserInfo;
import com.gogoyang.rpgapi.meta.user.service.IUserService;
import org.apache.tomcat.util.digester.ArrayStack;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final ICommonBusinessService iCommonBusinessService;

    @Autowired
    public SecretaryMatchBusinessService(IAdminService iAdminService,
                                         IJobService iJobService,
                                         IJobApplyService iJobApplyService,
                                         IAccountService iAccountService,
                                         IUserService iUserService,
                                         ICommonBusinessService iCommonBusinessService) {
        this.iAdminService = iAdminService;
        this.iJobService = iJobService;
        this.iJobApplyService = iJobApplyService;
        this.iAccountService = iAccountService;
        this.iUserService = iUserService;
        this.iCommonBusinessService = iCommonBusinessService;
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
        ArrayList<Job> jobs=iJobService.listJobByStatus(JobStatus.MATCHING, pageIndex, pageSize);

        Map out=new HashMap();
        out.put("jobs", jobs);
        return out;
    }

    @Override
    public Map listApplyByJobId(Map in) throws Exception {
        String token = in.get("token").toString();
        String jobId =  in.get("jobId").toString();
        Integer pageIndex=(Integer)in.get("pageIndex");
        Integer pageSize=(Integer)in.get("pageSize");

        /**
         * 当前操作用户必须是RPG秘书权限
         */
        Admin admin = iCommonBusinessService.getAdminByToken(token);
        if (!admin.getRoleType().equals(RoleType.SECRETARY.toString())) {
            throw new Exception("10040");
        }

        /**
         * read jobApply by jobId where processResult==null
         */
        Map qIn=new HashMap();
        qIn.put("jobId", jobId);
        qIn.put("status", LogStatus.PENDING);
        Integer offset=(pageIndex-1);
        qIn.put("offset", offset);
        qIn.put("size", pageSize);
        ArrayList<JobApply> jobApplies=iJobApplyService.listJobApply(qIn);

        Map out = new HashMap();
        out.put("applyList", jobApplies);
        return out;
    }

    /**
     * 同意用户的任务申请，任务交易成功
     * @param in
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
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
        String applyId=in.get("applyId").toString();

        //检查用户权限，必须是秘书
        Admin admin=iCommonBusinessService.getAdminByToken(token);
        if(admin==null){
            throw new Exception("10004");
        }
        if(!admin.getRoleType().equals(RoleType.SECRETARY.toString())){
            throw new Exception("10040");
        }

        //读取申请，保存为成功
        Map qIn=new HashMap();
        qIn.put("jobApplyId", applyId);
        JobApply jobApply=iJobApplyService.getJobApply(qIn);
        if(jobApply==null){
            throw new Exception("10109");
        }
        jobApply.setProcessUserId(admin.getAdminId());
        jobApply.setProcessTime(new Date());
        jobApply.setStatus(LogStatus.ACCEPT.toString());
        iJobApplyService.updateJobApply(jobApply);

        //读取任务，保存乙方信息，任务状态为进行中
        Job job=iJobService.getJobTinyByJobId(jobApply.getJobId());
        if(job==null){
            throw new Exception("10112");
        }
        Job jobEdit=new Job();
        jobEdit.setJobId(job.getJobId());
        jobEdit.setStatus(JobStatus.PROGRESS.toString());
        jobEdit.setContractTime(jobApply.getProcessTime());
        jobEdit.setPartyBId(jobApply.getApplyUserId());
        iJobService.updateJob(jobEdit);

        //把任务金额转给乙方
        UserInfo userB=iUserService.getUserByUserId(jobApply.getApplyUserId());
        if(userB==null){
            throw new Exception("10019");
        }
        Account account=new Account();
        account.setAccountId(GogoTools.UUID());
        account.setAmount(job.getPrice());
        account.setCreatedTime(jobApply.getProcessTime());
        account.setType(AccountType.APPLY_SUCCESS.toString());
        account.setUserId(jobApply.getApplyUserId());
        account.setJobId(jobApply.getJobId());
        iAccountService.createAccount(account);

        //刷新乙方的账户余额
        iCommonBusinessService.sumUserAccount(jobApply.getApplyUserId());

        /**
         * 处理其他用户的申请
         * 查询jobApply，jobId，status=PENDING, applyUserId!=applyId（乙方）
         */
        qIn=new HashMap();
        qIn.put("jobId", jobApply.getJobId());
        qIn.put("status", LogStatus.PENDING.toString());
        ArrayList<JobApply> otherApplies=iJobApplyService.listJobApply(qIn);
        for(int i=0;i<otherApplies.size();i++){
            if(!otherApplies.get(i).getApplyUserId().equals(jobApply.getApplyUserId())) {
                JobApply otherApply=new JobApply();
                otherApply.setJobApplyId(otherApplies.get(i).getJobApplyId());
                otherApply.setStatus(LogStatus.ACCEPT_BY_OTHERS.toString());
                otherApply.setProcessTime(jobApply.getProcessTime());
                otherApply.setProcessUserId(admin.getAdminId());
                iJobApplyService.updateJobApply(otherApply);
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void rejectApply(Map in) throws Exception {
        /**
         * check authority
         * load apply
         * set to reject
         */
        String token=in.get("token").toString();
        Integer applyId=(Integer)in.get("applyId");
        String remark=(String)in.get("remark");

        Admin admin=iCommonBusinessService.getAdminByToken(token);

        if(!admin.getRoleType().equals(RoleType.SECRETARY)){
            //当前用户不是秘书，不能操作
            throw new Exception("10034");
        }

        Map qIn=new HashMap();
        qIn.put("jobApplyId", applyId);
        JobApply jobApply=iJobApplyService.getJobApply(qIn);
        if(jobApply==null){
            throw new Exception("10109");
        }

        jobApply.setStatus(LogStatus.REJECT.toString());
        jobApply.setProcessTime(new Date());
        jobApply.setProcessUserId(admin.getAdminId());
        jobApply.setProcessRemark(remark);

        iJobApplyService.updateJobApply(jobApply);
    }

    @Override
    public Map getApplyJobTiny(Map in) throws Exception {
        String token=in.get("token").toString();
        String jobId=in.get("jobId").toString();

        Admin admin=iCommonBusinessService.getAdminByToken(token);

        Job job=iJobService.getJobTinyByJobId(jobId);

        Map out=new HashMap();
        out.put("job", job);

        return out;
    }

    @Override
    public Map getApplyJobDetail(Map in) throws Exception {
        String token=in.get("token").toString();
        String jobId=in.get("jobId").toString();

        UserInfo user=iCommonBusinessService.getUserByToken(token);

        Job job=iJobService.getJobDetailByJobId(jobId);

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

        Admin admin=iCommonBusinessService.getAdminByToken(token);

        Map qIn=new HashMap();
        qIn.put("jobApplyId", userId);
        Integer offset=(pageIndex-1)*pageSize;
        qIn.put("offset", offset);
        qIn.put("size", pageSize);
        ArrayList<JobApply> jobApplies=iJobApplyService.listJobApply(qIn);

        ArrayList<Map<String, Object>> list=new ArrayStack<Map<String, Object>>();
        for(int i=0;i<jobApplies.size();i++){
            JobApply jobApply=jobApplies.get(i);
            Map map=new HashMap();
            map.put("jobId", jobApply.getJobId());
            map.put("applyId", jobApply.getJobApplyId());
            map.put("applyUserId", jobApply.getApplyUserId());
            UserInfo user=iUserService.getUserByUserId(jobApply.getApplyUserId());
            if(user.getRealName()!=null) {
                map.put("username", jobApply.getApplyUserName());
            }else {
                map.put("username", user.getEmail());
            }
            map.put("applyTime", jobApply.getApplyTime());
            map.put("processUserId", jobApply.getProcessUserId());
            map.put("processResult", jobApply.getStatus());
            map.put("processTime", jobApply.getProcessTime());
            Job job=iJobService.getJobTinyByJobId(jobApply.getJobId());
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
        String applyId=in.get("applyId").toString();

        Admin admin=iAdminService.getAdminByToken(token);
        if(admin==null){
            throw new Exception("10004");
        }

        Map qIn=new HashMap();
        qIn.put("jobApplyId", applyId);
        JobApply jobApply=iJobApplyService.getJobApply(qIn);

        Job job=iJobService.getJobTinyByJobId(jobApply.getJobId());

        Map out=new HashMap();
        out.put("title", job.getTitle());
        out.put("applyTime", jobApply.getApplyTime());
        out.put("applyUser", jobApply.getApplyUserName());
        out.put("content", jobApply.getContent());
        out.put("status", jobApply.getStatus());
        out.put("processRemark", jobApply.getProcessRemark());
        out.put("processUserId", jobApply.getProcessUserId());
        out.put("processTime", jobApply.getProcessTime());

        return out;
    }
}

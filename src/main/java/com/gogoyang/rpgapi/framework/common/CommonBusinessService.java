package com.gogoyang.rpgapi.framework.common;

import com.fasterxml.jackson.databind.node.DoubleNode;
import com.gogoyang.rpgapi.framework.common.IRPGFunction;
import com.gogoyang.rpgapi.framework.constant.GogoActType;
import com.gogoyang.rpgapi.meta.account.service.IAccountService;
import com.gogoyang.rpgapi.meta.admin.entity.Admin;
import com.gogoyang.rpgapi.meta.admin.service.IAdminService;
import com.gogoyang.rpgapi.meta.job.entity.Job;
import com.gogoyang.rpgapi.meta.job.service.IJobService;
import com.gogoyang.rpgapi.meta.user.entity.UserInfo;
import com.gogoyang.rpgapi.meta.user.service.IUserService;
import com.gogoyang.rpgapi.meta.userAction.entity.UserActionLog;
import com.gogoyang.rpgapi.meta.userAction.service.IUserActionLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class CommonBusinessService implements ICommonBusinessService {
    private final IUserService iUserService;
    private final IRPGFunction irpgFunction;
    private final IUserActionLogService iUserActionLogService;
    private final IAdminService iAdminService;
    private final IJobService iJobService;
    private final IAccountService iAccountService;

    public CommonBusinessService(IUserService iUserService,
                                 IRPGFunction irpgFunction,
                                 IUserActionLogService iUserActionLogService,
                                 IAdminService iAdminService,
                                 IJobService iJobService,
                                 IAccountService iAccountService) {
        this.iUserService = iUserService;
        this.irpgFunction = irpgFunction;
        this.iUserActionLogService = iUserActionLogService;
        this.iAdminService = iAdminService;
        this.iJobService = iJobService;
        this.iAccountService = iAccountService;
    }

    @Override
    public UserInfo getUserByToken(String token) throws Exception {
        UserInfo user = iUserService.getUserByToken(token);
        if (user == null) {
            //读取用户信息失败
            throw new Exception("10028");
        }
        return user;
    }

    /**
     * 根据jobId查询job
     * 如果没查到，就中断程序，返回错误码
     * @param jobId
     * @return
     * @throws Exception
     */
    @Override
    public Job getJobTinyByJobId(String jobId) throws Exception {
        Job job = iJobService.getJobTinyByJobId(jobId);
        if(job==null){
            //没有查找到Job
            throw new Exception("30001");
        }
        return job;
    }

    /**
     * 根据jobId查询job
     * 如果没查到，就中断程序，返回错误码
     * @param jobId
     * @return
     * @throws Exception
     */
    @Override
    public Job getJobDetailByJobId(String jobId) throws Exception {
        Job job = iJobService.getJobDetailByJobId(jobId);
        if(job==null){
            //没有查找到Job
            throw new Exception("30001");
        }
        return job;
    }

    /**
     * 记录用户行为日志
     *
     * @param in
     * @throws Exception
     */
    @Transactional(rollbackOn = Exception.class)
    @Override
    public void createUserActionLog(Map in) throws Exception {
        GogoActType gogoActType = (GogoActType) in.get("GogoActType");
        String userId = (String) in.get("userId");
        String device = (String) in.get("device");
        String ipAddress = (String) in.get("ipAddress");
        HashMap memoMap = (HashMap) in.get("memo");
        String os = (String) in.get("os");
        String token = (String) in.get("token");

        UserInfo userInfo = null;
        if (userId != null) {
            userInfo = getUserByUserId(userId);
        } else {
            if (token != null) {
                userInfo = getUserByToken(token);
            }
        }

        UserActionLog userActLog = new UserActionLog();
        userActLog.setActType(gogoActType.toString());
        userActLog.setCreateTime(new Date());
        if (userInfo != null) {
            userActLog.setUserId(userInfo.getUserId().toString());
        }
        userActLog.setMemo(irpgFunction.convertMapToString(memoMap));
        userActLog.setUserActionLogId(irpgFunction.UUID().toString());
        iUserActionLogService.createUserActionLog(userActLog);
    }

    @Override
    public Admin getAdminByToken(String token) throws Exception {
        Admin admin = iAdminService.getAdminByToken(token);
        if (admin == null) {
            throw new Exception("20001");
        }
        return admin;
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public Map sumUserAccount(String userId) throws Exception {
        Map qIn=new HashMap();
        qIn.put("userId", userId);
        Map sum=iAccountService.sumAccountByType(qIn);

        Double accountIn=0.0;
        Double accountOut=0.0;
        Double accountBalance=0.0;

        //乙方获得的任务合同总额
        Double APPLY_SUCCESS=(Double)sum.get("APPLY_SUCCESS");
        if(APPLY_SUCCESS==null){
            APPLY_SUCCESS=0.0;
        }
        accountIn+=APPLY_SUCCESS;
        accountBalance+=APPLY_SUCCESS;

        //充值总额
        Double TOP_UP=(Double)sum.get("TOP_UP");
        if(TOP_UP==null){
            TOP_UP=0.0;
        }
        accountBalance+=TOP_UP;

        //取现总额
        Double WITHDRAW=(Double)sum.get("WITHDRAW");
        if(WITHDRAW==null){
            WITHDRAW=0.0;
        }
        accountOut+=WITHDRAW;
        accountBalance-=WITHDRAW;

        //发布任务总额
        Double PUBLISH=(Double)sum.get("PUBLISH");
        if(PUBLISH==null){
            PUBLISH=0.0;
        }
        accountOut+=PUBLISH;
        accountBalance-=PUBLISH;

        //退款支出总额
        Double REFUND_OUT=(Double)sum.get("REFUND_OUT");
        if(REFUND_OUT==null){
            REFUND_OUT=0.0;
        }
        accountOut+=REFUND_OUT;
        accountBalance-=REFUND_OUT;

        //退款收入总额
        Double REFUND_IN=(Double)sum.get("REFUND_IN");
        if(REFUND_IN==null){
            REFUND_IN=0.0;
        }
        accountIn+=REFUND_IN;
        accountBalance+=REFUND_IN;

        //更新用户表账户余额
        UserInfo userInfo=new UserInfo();
        userInfo.setUserId(userId);
        userInfo.setAccount(accountBalance);
        userInfo.setAccountIn(accountIn);
        userInfo.setAccountOut(accountOut);
        iUserService.updateUserInfo(userInfo);

        Map out=new HashMap();
        out.put("accountIn", accountIn);
        out.put("accountOut", accountOut);
        out.put("accountBalance", accountBalance);

        return out;
    }

    public UserInfo getUserByUserId(String userId) throws Exception {
        UserInfo userInfo = iUserService.getUserByUserId(userId);
        if (userInfo == null) {
            throw new Exception("10028");
        }
        return userInfo;
    }
}

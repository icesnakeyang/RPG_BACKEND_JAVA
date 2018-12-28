package com.gogoyang.rpgapi.business.admin.secretary.match.service;

import com.gogoyang.rpgapi.framework.constant.JobStatus;
import com.gogoyang.rpgapi.framework.constant.LogStatus;
import com.gogoyang.rpgapi.framework.constant.RoleType;
import com.gogoyang.rpgapi.meta.admin.entity.Admin;
import com.gogoyang.rpgapi.meta.admin.service.IAdminService;
import com.gogoyang.rpgapi.meta.apply.entity.JobApply;
import com.gogoyang.rpgapi.meta.apply.service.IJobApplyService;
import com.gogoyang.rpgapi.meta.job.entity.Job;
import com.gogoyang.rpgapi.meta.job.service.IJobService;
import com.gogoyang.rpgapi.meta.match.entity.JobMatch;
import com.gogoyang.rpgapi.meta.match.service.IJobMatchService;
import com.gogoyang.rpgapi.meta.user.userInfo.entity.UserInfo;
import com.gogoyang.rpgapi.meta.user.userInfo.service.IUserInfoService;
import org.omg.CORBA.OBJECT_NOT_EXIST;
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
    private final IUserInfoService iUserInfoService;
    private final IJobMatchService iJobMatchService;

    @Autowired
    public SecretaryMatchBusinessService(IAdminService iAdminService, IJobService iJobService,
                                         IJobApplyService iJobApplyService,
                                         IUserInfoService iUserInfoService,
                                         IJobMatchService iJobMatchService) {
        this.iAdminService = iAdminService;
        this.iJobService = iJobService;
        this.iJobApplyService = iJobApplyService;
        this.iUserInfoService = iUserInfoService;
        this.iJobMatchService = iJobMatchService;
    }

    /**
     * list users history
     * @param in
     * @return
     * @throws Exception
     */
    @Override
    public Map listApplyHistory(Map in) throws Exception {
        String token=in.get("token").toString();
        Integer pageIndex=(Integer)in.get("pageIndex");
        Integer pageSize=(Integer)in.get("pageSize");
        Integer userId=(Integer)in.get("userId");

        Admin admin=iAdminService.loadAdminByToken(token);
        if(admin==null){
            throw new Exception("10004");
        }

        Page<JobApply> jobApplies=iJobApplyService.listJobapplybyUserId(userId, pageIndex, pageSize);

        ArrayList<Map<String, Object>> applyHistoryList=new ArrayList<Map<String, Object>>();
        for(int i=0;i<jobApplies.getContent().size();i++){
            JobApply apply=jobApplies.getContent().get(i);
            /**
             * output: job title, price, apply time, apply result, job status
             */
            Map map=new HashMap();
            Job job=iJobService.getJobByJobIdTiny(apply.getJobId());
            map.put("title", job.getTitle());
            map.put("price", job.getPrice());
            map.put("result", apply.getProcessResult());
            map.put("status", job.getStatus());
            map.put("applyTime", apply.getApplyTime());
            applyHistoryList.add(map);
        }

        Map out=new HashMap();
        out.put("applyHistoryList", applyHistoryList);
        return out;
    }

    /**
     * list new applies
     * @param in
     * @return
     * @throws Exception
     */
    @Override
    public Map listJobMatching(Map in) throws Exception {
        /**
         * list job status=matching
         */
        String token=in.get("token").toString();
        Integer pageIndex=(Integer)in.get("pageIndex");
        Integer pageSize=(Integer)in.get("pageSize");

        Admin admin=iAdminService.loadAdminByToken(token);
        if(admin==null){
            throw new Exception("10004");
        }

        Page<Job> jobs=iJobService.listJobByStatus(JobStatus.MATCHING, pageIndex, pageSize);

        Map out=new HashMap();
        out.put("newApplyList", jobs);
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
        Admin admin = iAdminService.loadAdminByToken(token);
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
            UserInfo userInfo=iUserInfoService.getUserByUserId(apply.getApplyUserId());
            if(userInfo.getRealName()!=null){
                map.put("applyUser", userInfo.getRealName());
            }else {
                map.put("applyUser", userInfo.getEmail());
            }
            map.put("applyUserId", userInfo.getUserId());
            map.put("applyTime", apply.getApplyTime());
            map.put("applyId", apply.getJobApplyId());
            applyList.add(map);
        }

        Map out = new HashMap();
        out.put("applyList", applyList);
        return out;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void agreeApply(Map in) throws Exception {

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

        Admin admin=iAdminService.loadAdminByToken(token);
        if(admin==null){
            throw new Exception("10004");
        }

        JobApply jobApply=iJobApplyService.getJobApplyByJobApplyId(applyId);
        if(jobApply==null){
            throw new Exception("")
        }
    }
}

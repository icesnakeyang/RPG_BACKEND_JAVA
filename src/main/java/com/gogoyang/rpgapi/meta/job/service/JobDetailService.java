package com.gogoyang.rpgapi.meta.job.service;

import com.gogoyang.rpgapi.meta.apply.service.IJobApplyService;
import com.gogoyang.rpgapi.meta.job.dao.JobDetailDao;
import com.gogoyang.rpgapi.meta.job.entity.JobDetail;
import com.gogoyang.rpgapi.meta.task.service.ITaskService;
import com.gogoyang.rpgapi.meta.user.userInfo.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

@Service
public class JobDetailService implements IJobDetail {
    private IJobService iJobService;
    private IUserInfoService iUserInfoService;
    private ITaskService iTaskService;
    private IJobApplyService iJobApplyService;
    private final JobDetailDao jobDetailDao;

    @Autowired
    public JobDetailService(JobDetailDao jobDetailDao) {
        this.jobDetailDao = jobDetailDao;
    }

    @Override
    public Map loadJobDetail(Map in) throws Exception {
        //todo
        /**
         * load data from IJobService, IUserInfoService, ITaskService
        title: 任务标题——job
        code: 项目代号——job
        party A: 甲方——userInfo
        party B: 乙方——userInfo
        publish time: 发布时间——job
        contract time: 签约时间——job
        price：任务佣金——job
        days：任务期限——job
        days pass: 已进行天数——计算
        days left: 剩余天数——计算
        detail: 任务详情——task
        applies: 申请人数——job
        matches: 分配人数——job
        views: 查看人数——job
         **/

        Map out=new HashMap();
        /**
         * out.put("job", job)
         * out.put("partyA", userInfo)
         * out.put("partyB", userInfo)
         * out.put("detail",task)
         */

        return out;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void applyJob(Map in) throws Exception {
        //todo
        /**
         * IJobApplyService to createJobApply(jobApply)
         * jobApply.jobId=jobId
         * jobApply.applyUserId=userId
         * jobApply.applyRemark=remark
         */
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void agreeMatch(Map in) throws Exception {

    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void rejectMatch(Map in) throws Exception {

    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void updateJobDetail(JobDetail jobDetail) throws Exception {
        if(jobDetail.getJobId()==null){
            throw new Exception("10099");
        }
        jobDetailDao.save(jobDetail);
    }

    @Override
    public JobDetail getJobDetailByJobId(Integer jobId) throws Exception {
        JobDetail detail=jobDetailDao.findByJobId(jobId);
        return detail;
    }
}

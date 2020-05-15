package com.gogoyang.rpgapi.meta.apply.service;

import com.gogoyang.rpgapi.framework.constant.LogStatus;
import com.gogoyang.rpgapi.meta.apply.dao.JobApplyDao;
import com.gogoyang.rpgapi.meta.apply.entity.JobApply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class JobApplyService implements IJobApplyService {
    private final JobApplyDao jobApplyDao;

    @Autowired
    public JobApplyService(JobApplyDao jobApplyDao) {
        this.jobApplyDao = jobApplyDao;
    }

    /**
     * 创建一个任务申请
     *
     * @param jobApply
     * @throws Exception
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public void insertJobApply(JobApply jobApply) throws Exception {
        if(jobApply.getJobApplyId()!=null){
            throw new Exception("10026");
        }
        jobApplyDao.createJobApply(jobApply);
    }

    @Override
    public JobApply getJobApply(Map qIn) throws Exception {
        JobApply jobApply=jobApplyDao.getJobApply(qIn);
        return jobApply;
    }

    @Override
    ArrayList<JobApply> listJobApply(Map qIn) throws Exception{
        ArrayList<JobApply> jobApplies=jobApplyDao.listJobApply(qIn);
        return jobApplies;
    }

    /**
     * count how many users are applying this job
     *
     * @param jobId
     * @return
     * @throws Exception
     */
    @Override
    public Integer countApplyUsers(String jobId) throws Exception {
        Map qIn=new HashMap();
        qIn.put("jobId", jobId);
        qIn.put("status", LogStatus.PENDING);
        Integer total=jobApplyDao.totalJobApply(qIn);
        return total;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void updateJobApply(JobApply jobApply) throws Exception {
        if(jobApply.getJobApplyId()==null){
            throw new Exception("10010");
        }
        jobApplyDao.updateJobApply(jobApply);
    }


}

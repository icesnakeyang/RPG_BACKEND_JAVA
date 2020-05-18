package com.gogoyang.rpgapi.meta.apply.service;

import com.gogoyang.rpgapi.framework.constant.LogStatus;
import com.gogoyang.rpgapi.meta.apply.dao.JobApplyDao;
import com.gogoyang.rpgapi.meta.apply.entity.JobApply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional(rollbackFor = Exception.class)
    public void insertJobApply(JobApply jobApply) throws Exception {
        jobApplyDao.createJobApply(jobApply);
    }

    /**
     * 读取一个任务申请信息
     * @param qIn
     * jobApplyId
     * @return
     * @throws Exception
     */
    @Override
    public JobApply getJobApply(Map qIn) throws Exception {
        JobApply jobApply=jobApplyDao.getJobApply(qIn);
        return jobApply;
    }

    @Override
    public ArrayList<JobApply> listJobApply(Map qIn) throws Exception{
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
    public ArrayList<JobApply> listMyApplies(Integer userId) throws Exception {
        return null;
    }

    @Override
    public void matchJobApply(JobApply jobApply) throws Exception {

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateJobApply(JobApply jobApply) throws Exception {
        if(jobApply.getJobApplyId()==null){
            throw new Exception("10010");
        }
        jobApplyDao.updateJobApply(jobApply);
    }


}

package com.gogoyang.rpgapi.job.meta.jobApply.service;

import com.gogoyang.rpgapi.constant.LogStatus;
import com.gogoyang.rpgapi.job.meta.jobApply.dao.JobApplyDao;
import com.gogoyang.rpgapi.job.meta.jobApply.entity.JobApply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;

@Service
public class JobApplyService implements IJobApplyService {
    private final JobApplyDao jobApplyDao;

    @Autowired
    public JobApplyService(JobApplyDao jobApplyDao) {
        this.jobApplyDao = jobApplyDao;
    }

    /**
     * 读取任务的所有申请日志
     *
     * @param jobId
     * @return
     * @throws Exception
     */
    @Override
    public ArrayList<JobApply> loadJobApplyByJobId(Integer jobId) throws Exception {
        ArrayList<JobApply> jobApplies = jobApplyDao.findAllByJobIdAndProcessResultIsNull(jobId);

        return jobApplies;
    }

    /**
     * 创建一个任务申请
     *
     * @param jobApply
     * @throws Exception
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public void createJobApply(JobApply jobApply) throws Exception {
        jobApplyDao.save(jobApply);
    }


    /**
     * 查询用户是否已申请了该任务
     * check whether userId has applied the jobId
     *
     * @param userId
     * @param jobId
     * @return
     */
    @Override
    public boolean isApplied(Integer userId, Integer jobId) throws Exception {
        JobApply jobApply = jobApplyDao.findByApplyUserIdAndJobIdAndProcessResultIsNull(userId, jobId);
        if (jobApply != null) {
            //the job has applied by current user already.
            return true;
        }
        return false;
    }

    /**
     * count how many users applied this job
     *
     * @param jobId
     * @return
     * @throws Exception
     */
    @Override
    public Integer countApplyUsers(Integer jobId) throws Exception {
        /**
         * 统计有多少用户申请了这个任务
         *
         */
        ArrayList<JobApply> jobApplyList = jobApplyDao.findAllByJobIdAndProcessResultIsNull(jobId);
        if (jobApplyList != null) {
            return jobApplyList.size();
        }
        return 0;
    }

    /**
     * load jobApply logs applied by userId
     *
     * @param userId
     * @return
     * @throws Exception
     */
    @Override
    public ArrayList<JobApply> loadMyApplies(Integer userId) throws Exception {
        ArrayList<JobApply> applies = jobApplyDao.findAllByApplyUserIdAndProcessResultIsNull(userId);
        return applies;
    }

    /**
     * 当RPG秘书分配了任务后，系统需要处理用户的任务申请日志，
     * 修改申请日志的处理结果和处理时间。
     * @param jobApply
     * @throws Exception
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public void matchJobApply(JobApply jobApply) throws Exception {
        JobApply updateJobApply = jobApplyDao.findByApplyUserIdAndJobIdAndProcessResultIsNull(
                jobApply.getApplyUserId(), jobApply.getJobId());
        if (updateJobApply == null) {
            throw new Exception("error safnnxcv9872934:" + "no such job jobApply record");
        }
        updateJobApply.setProcessTime(new Date());
        updateJobApply.setProcessUserId(-1);
        updateJobApply.setProcessResult(LogStatus.MATCHED);
        jobApplyDao.save(updateJobApply);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void updateJobApply(JobApply jobApply) throws Exception {
        if(jobApply.getJobApplyId()==null){
            throw new Exception("10010");
        }
        jobApplyDao.save(jobApply);
    }
}

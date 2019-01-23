package com.gogoyang.rpgapi.meta.apply.service;

import com.gogoyang.rpgapi.framework.constant.JobStatus;
import com.gogoyang.rpgapi.framework.constant.LogStatus;
import com.gogoyang.rpgapi.meta.apply.dao.JobApplyDao;
import com.gogoyang.rpgapi.meta.apply.entity.JobApply;
import org.omg.CORBA.INTERNAL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
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
        jobApplyDao.save(jobApply);
    }

    @Override
    public JobApply getJobApplyByApplyId(Integer applyId) throws Exception {
        JobApply jobApply=jobApplyDao.findByJobApplyId(applyId);
        return jobApply;
    }

    @Override
    public JobApply getJobApplyByJobApplyId(Integer applyId) throws Exception {
        JobApply jobApply=jobApplyDao.findByJobApplyId(applyId);
        return jobApply;
    }

    @Override
    public ArrayList<JobApply> listJobApplyByNotProcesJobId(Integer jobId) throws Exception {
        ArrayList<JobApply> jobApplies=jobApplyDao.findAllByJobIdAndProcessResultIsNull(jobId);
        return jobApplies;
    }

    /**
     * 根据userId和jobId读取未处理的申请
     * @param userId
     * @param jobId
     * @return
     * @throws Exception
     */
    @Override
    public JobApply loadJobApplyByUserIdAndJobId(Integer userId, Integer jobId) throws Exception {
        JobApply jobApply=jobApplyDao.findByApplyUserIdAndJobIdAndProcessResultIsNull(userId, jobId);
        return  jobApply;
    }

    /**
     * 读取任务的所有申请日志
     *
     * @param jobId
     * @return
     * @throws Exception
     */
    @Override
    public ArrayList<JobApply> listJobApplyByJobId(Integer jobId) throws Exception {
        ArrayList<JobApply> jobApplies = jobApplyDao.findAllByJobIdAndProcessResultIsNull(jobId);

        return jobApplies;
    }

    @Override
    public Page<JobApply> listJobApply(Map qIn) throws Exception {
        Integer pageIndex=(Integer)qIn.get("pageIndex");
        Integer pageSize=(Integer)qIn.get("pageSize");

        Sort sort=new Sort(Sort.Direction.DESC, "jobApplyId");
        Pageable pageable= new PageRequest(pageIndex, pageSize, sort);
        //查找所有任务申请
        Page<JobApply> jobApplies=jobApplyDao.findAllByProcessResultIsNull(pageable);

        return jobApplies;
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
            //the common has applied by current user already.
            return true;
        }
        return false;
    }

    /**
     * count how many users applied this common
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
    public ArrayList<JobApply> listMyApplies(Integer userId) throws Exception {
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
            throw new Exception("error safnnxcv9872934:" + "no such common jobApply record");
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

    /**
     * read apply common by userId
     * @param userId
     * @param pageIndex
     * @param pageSize
     * @return
     * @throws Exception
     */
    @Override
    public Page<JobApply> listJobapplybyUserId(Integer userId, Integer pageIndex, Integer pageSize) throws Exception {
        Sort sort=new Sort(Sort.Direction.DESC, "jobApplyId");
        Pageable pageable=new PageRequest(pageIndex, pageSize, sort);
        Page<JobApply> jobApplies=jobApplyDao.findAllByApplyUserId(userId, pageable);
        return jobApplies;
    }

    /**
     * read and return all party b un-read new job
     * @param userId
     * @return
     * @throws Exception
     */
    @Override
    public ArrayList<JobApply> listPartyBNewJob(Integer userId) throws Exception {
        ArrayList<JobApply> applies=jobApplyDao.findAllByProcessResultReadTimeIsNullAndApplyUserIdAndProcessResult(userId, LogStatus.ACCEPT);
        return applies;
    }
}

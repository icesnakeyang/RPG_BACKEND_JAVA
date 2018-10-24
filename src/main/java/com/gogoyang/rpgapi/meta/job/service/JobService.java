package com.gogoyang.rpgapi.meta.job.service;

import com.gogoyang.rpgapi.business.job.complete.service.ICompleteBusinessService;
import com.gogoyang.rpgapi.business.job.jobLog.service.IJobLogBusinessService;
import com.gogoyang.rpgapi.framework.constant.JobStatus;
import com.gogoyang.rpgapi.meta.job.dao.JobDao;
import com.gogoyang.rpgapi.meta.job.dao.JobDetailDao;
import com.gogoyang.rpgapi.meta.job.entity.Job;
import com.gogoyang.rpgapi.meta.job.entity.JobDetail;
import com.gogoyang.rpgapi.meta.apply.service.IJobApplyService;
import com.gogoyang.rpgapi.meta.match.service.IJobMatchService;
import com.gogoyang.rpgapi.meta.task.service.ITaskService;
import com.gogoyang.rpgapi.meta.user.userInfo.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

@Service
class JobService implements IJobService {

    private final JobDao jobDao;
    private final IUserInfoService iUserInfoService;
    private final ITaskService iTaskService;
    private final IJobApplyService iJobApplyService;
    private final IJobMatchService iJobMatchService;
    private final JobDetailDao jobDetailDao;

    @Autowired
    public JobService(JobDao jobDao, IUserInfoService iUserInfoService, ITaskService iTaskService,
                      IJobApplyService iJobApplyService, IJobMatchService iJobMatchService,
                      JobDetailDao jobDetailDao) {
        this.jobDao = jobDao;
        this.iUserInfoService = iUserInfoService;
        this.iTaskService = iTaskService;
        this.iJobApplyService = iJobApplyService;
        this.iJobMatchService = iJobMatchService;
        this.jobDetailDao = jobDetailDao;
    }

    /**
     * 创建一个Job
     *
     * @param job
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public Job insertJob(Job job) throws Exception {
        //确保jobId为null，否则为修改
        if (job.getJobId() != null) {
            throw new Exception("10032");
        }
        if (job.getTitle() == null) {
            throw new Exception("10032");
        }
        job = jobDao.save(job);

        //保存jobDetail表
        JobDetail jobDetail = new JobDetail();
        jobDetail.setJobId(job.getJobId());
        jobDetail.setDetail(job.getDetail());
        jobDetailDao.save(jobDetail);

        return job;
    }

    /**
     * 读取Job的详细信息，包括jobDetail
     *
     * @param jobId
     * @return
     * @throws Exception
     */
    @Override
    public Job loadJobByJobId(Integer jobId) throws Exception {
        Job job = jobDao.findByJobId(jobId);

        job.setPartyAName(iUserInfoService.getUserName(job.getPartyAId()));
        job.setPartyBName(iUserInfoService.getUserName(job.getPartyBId()));

        JobDetail jobDetail = jobDetailDao.findByJobId(job.getJobId());
        job.setDetail(jobDetail.getDetail());

        return job;
    }

    /**
     * 读取job，不带detail
     *
     * @param jobId
     * @return
     * @throws Exception
     */
    @Override
    public Job loadJobByJobIdTiny(Integer jobId) throws Exception {
        Job job = jobDao.findByJobId(jobId);
        return job;
    }

    /**
     * 根据taskId读取job
     *
     * @param taskId
     * @return
     * @throws Exception
     */
    @Override
    public Job getJobByTaskId(Integer taskId) throws Exception {
        Job job = jobDao.findByTaskId(taskId);
        return job;
    }

    /**
     * 根据jobStatus读取jobs
     * read jobs by jobStatus
     * paginate the list
     *
     * @param pageIndex
     * @param pageSize
     * @return
     * @throws Exception
     */
    @Override
    public Page<Job> loadJobByStatus(JobStatus jobStatus, Integer pageIndex, Integer pageSize) throws Exception {
        Sort sort = new Sort(Sort.Direction.DESC, "jobId");
        Pageable pageable = new PageRequest(pageIndex, pageSize, sort);
        Page<Job> jobs = jobDao.findAllByStatus(jobStatus, pageable);
        return jobs;
    }

    /**
     * 修改job
     *
     * @param job
     * @throws Exception
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public void updateJob(Job job) throws Exception {
        if (job.getJobId() == null) {
            throw new Exception("10010");
        }
        jobDao.save(job);
    }

    /**
     * 读取所有我是甲方的任务
     *
     * @param userId
     * @param jobStatus
     * @param pageIndex
     * @param pageSize
     * @return
     * @throws Exception
     */
    @Override
    public Page<Job> loadPartyAJob(Integer userId, JobStatus jobStatus, Integer pageIndex, Integer pageSize) throws Exception {
        Sort sort = new Sort(Sort.Direction.DESC, "jobId");
        Pageable pageable = new PageRequest(pageIndex, pageSize, sort);
        Page<Job> jobs = jobDao.findAllByPartyAIdAndStatus(userId, jobStatus, pageable);
        return jobs;
    }

    /**
     * 读取我是乙方的所有任务
     *
     * @param userId
     * @param jobStatus
     * @param pageIndex
     * @param pageSize
     * @return
     * @throws Exception
     */
    @Override
    public Page<Job> loadPartyBJob(Integer userId, JobStatus jobStatus,
                                   Integer pageIndex, Integer pageSize) throws Exception {
        Sort sort = new Sort(Sort.Direction.DESC, "jobId");
        Pageable pageable = new PageRequest(pageIndex, pageSize, sort);
        Page<Job> jobs = jobDao.findAllByPartyBIdAndStatus(userId, jobStatus, pageable);
        return jobs;
    }
}

package com.gogoyang.rpgapi.meta.job.service;

import com.gogoyang.rpgapi.meta.job.dao.JobDao;
import com.gogoyang.rpgapi.meta.job.dao.JobDetailDao;
import com.gogoyang.rpgapi.meta.job.entity.Job;
import com.gogoyang.rpgapi.meta.job.entity.JobDetail;
import com.gogoyang.rpgapi.meta.apply.entity.JobApply;
import com.gogoyang.rpgapi.meta.apply.service.IJobApplyService;
import com.gogoyang.rpgapi.meta.match.service.IJobMatchService;
import com.gogoyang.rpgapi.meta.task.entity.Task;
import com.gogoyang.rpgapi.meta.task.service.ITaskService;
import com.gogoyang.rpgapi.meta.user.userInfo.entity.UserInfo;
import com.gogoyang.rpgapi.meta.user.userInfo.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
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
    public JobService(JobDao jobDao, IUserInfoService iUserInfoService, ITaskService iTaskService, IJobApplyService iJobApplyService, IJobMatchService iJobMatchService, JobDetailDao jobDetailDao) {
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
        if(job.getJobId()!=null){
            throw new Exception("10032");
        }
        if(job.getTitle()==null){
            throw new Exception("10032");
        }
        job = jobDao.save(job);

        //保存jobDetail表
        JobDetail jobDetail=new JobDetail();
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

        UserInfo userInfo = iUserInfoService.loadUserByUserId(job.getPartyAId());
        job.setPartyAName(iUserInfoService.getUserName(userInfo.getUserId()));

        Task task = iTaskService.loadTaskByTaskId(job.getTaskId());
        job.setDetail(task.getDetail());

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
     * 读取还未被分配的job
     *
     * @param pageIndex
     * @param pageSize
     * @return
     * @throws Exception
     */
    @Override
    public Page<Job> loadJobUnMatch(Integer pageIndex, Integer pageSize) throws Exception {
        Sort sort = new Sort(Sort.Direction.DESC, "jobId");
        Pageable pageable = new PageRequest(pageIndex, pageSize, sort);
        Page<Job> jobs = jobDao.findAllByStatusIsNull(pageable);

        for (int i = 0; i < jobs.getContent().size(); i++) {
            jobs.getContent().get(i).setPartyAName(
                    iUserInfoService.getUserName(
                            jobs.getContent().get(i).getPartyAId())
            );
        }

        return jobs;
    }

    /**
     * 读取用户已经申请，但还没有成交的任务
     *
     * @param pageIndex
     * @param pageSize
     * @return
     * @throws Exception
     */
    @Override
    public ArrayList<Job> loadJobToMatch(Integer pageIndex, Integer pageSize) throws Exception {
        /**
         * 1、读取所有job，ismatch=false
         * 2、逐条查询jobApply，如果有，添加到list
         */
        Sort sort = new Sort(Sort.Direction.ASC, "jobId");
        Pageable pageable = new PageRequest(pageIndex, pageSize, sort);
        Page<Job> jobs = jobDao.findAllByStatusIsNull(pageable);

        ArrayList<Job> jobsOut = new ArrayList<Job>();
        for (int i = 0; i < jobs.getContent().size(); i++) {
            ArrayList<JobApply> jobApplies = iJobApplyService.loadJobApplyByJobId(
                    jobs.getContent().get(i).getJobId());
            if (jobApplies.size() > 0) {
                jobs.getContent().get(i).setPartyAName(iUserInfoService.getUserName(
                        jobs.getContent().get(i).getPartyAId()));
                jobs.getContent().get(i).setJobApplyNum(iJobMatchService.countMatchingUsers(
                        jobs.getContent().get(i).getJobId()));
                jobs.getContent().get(i).setJobApplyNum(iJobApplyService.countApplyUsers(
                        jobs.getContent().get(i).getJobId()));
                jobsOut.add(jobs.getContent().get(i));
            }
        }
        return jobsOut;
    }

    /**
     * 读取所有我申请的，还未处理的任务。
     *
     * @param userId
     * @return
     * @throws Exception
     */
    @Override
    public ArrayList loadMyApplyJob(Integer userId) throws Exception {
        ArrayList<JobApply> myApplyList = iJobApplyService.loadMyApplies(userId);
        ArrayList jobList = new ArrayList();
        Map mapOut = new HashMap();
        for (int i = 0; i < myApplyList.size(); i++) {
            Job job = jobDao.findByJobId(myApplyList.get(i).getJobId());
            if (job != null) {
                job.setPartyAName(iUserInfoService.getUserName(job.getPartyAId()));
                Integer applyNum = iJobApplyService.countApplyUsers(job.getJobId());
                Integer matchNum = iJobMatchService.countMatchingUsers(job.getJobId());
                Map theMap = new HashMap();
                theMap.put("job", job);
                theMap.put("apply", myApplyList.get(i));
                theMap.put("applyNum", applyNum);
                theMap.put("matchNum", matchNum);

                jobList.add(theMap);
            }
        }

        return jobList;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void updateJob(Job job) throws Exception {
        if(job.getJobId()==null){
            throw new Exception("10010");
        }
        jobDao.save(job);
    }
}

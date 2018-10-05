package com.gogoyang.rpgapi.job.job.service;

import com.gogoyang.rpgapi.job.job.dao.JobDao;
import com.gogoyang.rpgapi.job.job.entity.Job;
import com.gogoyang.rpgapi.job.jobApply.entity.JobApply;
import com.gogoyang.rpgapi.job.jobApply.service.IJobApplyService;
import com.gogoyang.rpgapi.job.jobMatch.service.IJobMatchService;
import com.gogoyang.rpgapi.task.entity.Task;
import com.gogoyang.rpgapi.task.service.ITaskService;
import com.gogoyang.rpgapi.user.userInfo.entity.UserInfo;
import com.gogoyang.rpgapi.user.userInfo.service.IUserInfoService;
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

    @Autowired
    public JobService(JobDao jobDao, IUserInfoService iUserInfoService, ITaskService iTaskService, IJobApplyService iJobApplyService, IJobMatchService iJobMatchService) {
        this.jobDao = jobDao;
        this.iUserInfoService = iUserInfoService;
        this.iTaskService = iTaskService;
        this.iJobApplyService = iJobApplyService;
        this.iJobMatchService = iJobMatchService;
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
    public Job createJob(Job job) throws Exception {
        /**
         * 1、新增一个job
         * 2、把job复制给task，保存
         */
        job = jobDao.save(job);

        Task task = new Task();
        task.setTaskId(job.getTaskId());
        task.setDetail(job.getDetail());
        task.setDays(job.getDays());
        task.setCode(job.getCode());
        task.setTitle(job.getTitle());
        iTaskService.updateTask(task);

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

        UserInfo userInfo = iUserInfoService.loadUserByUserId(job.getCreatedUserId());
        job.setCreatedUserName(iUserInfoService.getUserName(userInfo.getUserId()));

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
        Page<Job> jobs = jobDao.findAllByIsMatchIsNull(pageable);

        for (int i = 0; i < jobs.getContent().size(); i++) {
            jobs.getContent().get(i).setCreatedUserName(
                    iUserInfoService.getUserName(
                            jobs.getContent().get(i).getCreatedUserId())
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
        Page<Job> jobs = jobDao.findAllByIsMatchIsNull(pageable);

        ArrayList<Job> jobsOut = new ArrayList<Job>();
        for (int i = 0; i < jobs.getContent().size(); i++) {
            ArrayList<JobApply> jobApplies = iJobApplyService.loadJobApplyByJobId(
                    jobs.getContent().get(i).getJobId());
            if (jobApplies.size() > 0) {
                jobs.getContent().get(i).setCreatedUserName(iUserInfoService.getUserName(
                        jobs.getContent().get(i).getCreatedUserId()));
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
                job.setCreatedUserName(iUserInfoService.getUserName(job.getCreatedUserId()));
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
}
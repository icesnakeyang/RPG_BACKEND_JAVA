package com.gogoyang.rpgapi.job.service.impl;

import com.gogoyang.rpgapi.job.dao.JobApplyLogDao;
import com.gogoyang.rpgapi.job.dao.JobDao;
import com.gogoyang.rpgapi.job.entity.Job;
import com.gogoyang.rpgapi.job.entity.JobApplyLog;
import com.gogoyang.rpgapi.job.service.IJobService;
import com.gogoyang.rpgapi.job.vo.ApplyJobRequest;
import com.gogoyang.rpgapi.job.vo.CreateJobRequest;
import com.gogoyang.rpgapi.job.vo.CreateJobResponse;
import com.gogoyang.rpgapi.task.dao.TaskDetailDao;
import com.gogoyang.rpgapi.user.dao.UserDao;
import com.gogoyang.rpgapi.user.entity.User;
import com.gogoyang.rpgapi.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Service
public class JobService implements IJobService {

    private final JobDao jobDao;
    private final UserDao userDao;
    private final TaskDetailDao taskDetailDao;
    private final JobApplyLogDao jobApplyLogDao;

    @Autowired
    public JobService(JobDao jobDao, UserDao userDao, TaskDetailDao taskDetailDao, JobApplyLogDao jobApplyLogDao) {
        this.jobDao = jobDao;
        this.userDao = userDao;
        this.taskDetailDao = taskDetailDao;
        this.jobApplyLogDao = jobApplyLogDao;
    }

    @Override
    @Transactional
    public Response createJob(CreateJobRequest request) {
        Response response = new Response();
        CreateJobResponse jobResponse = new CreateJobResponse();
        jobResponse.setJobId(jobDao.save(request.toJob()).getJobId());
        response.setData(jobResponse);
        return response;
    }

    @Override
    public Response buildJobInfo(Integer id) {
        Job job = jobDao.findOne(id);
        User user = userDao.findByUserId(job.getCreatedUserId());
        job.setCreatedUserName(user.getUsername());
        job.setDetail(taskDetailDao.findByTaskId(job.getTaskId()).getDetail());
        Response response = new Response();
        response.setData(job);
        return response;
    }

    @Override
    public Response buildJobs(String category) {
        Response response = new Response();
        List<Job> jobs = jobDao.findAllByCategory(category);
        for (int i = 0; i < jobs.size(); i++) {
            User user = userDao.findByUserId(jobs.get(i).getCreatedUserId());
            jobs.get(i).setCreatedUserName(user.getUsername());
        }
        response.setData(jobs);
        return response;
    }

    public boolean canApplyJob(ApplyJobRequest request){
        Job job=jobDao.findByJobId(request.getJobId());
        if(job.getApplyJobLogId()==null){
            return true;
        }
        return false;
    }

    @Transactional(rollbackOn = Exception.class)
    public Response applyJob(ApplyJobRequest request) throws Exception{
        JobApplyLog applyLog = new JobApplyLog();
        applyLog.setApplyTime(new Date());
        User user=userDao.findByToken(request.getToken());
        applyLog.setApplyUserId(user.getUserId());
        applyLog.setJobId(request.getJobId());
        Response response = new Response();
        if(!canApplyJob(request)){
            return response;
        }
        Integer applyLogId=jobApplyLogDao.save(applyLog).getIds();
        Job job=jobDao.findByJobId(request.getJobId());
        if(job==null){
            throw new Exception("update apply job error");
        }
        job.setApplyJobLogId(applyLogId);
        jobDao.save(job);
        response.setData(applyLogId);
        return response;
    }
}

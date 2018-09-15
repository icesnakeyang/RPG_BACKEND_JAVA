package com.gogoyang.rpgapi.job.service.impl;

import com.gogoyang.rpgapi.job.dao.JobDao;
import com.gogoyang.rpgapi.job.entity.Job;
import com.gogoyang.rpgapi.job.service.IJobService;
import com.gogoyang.rpgapi.job.vo.CreateJobRequest;
import com.gogoyang.rpgapi.job.vo.CreateJobResponse;
import com.gogoyang.rpgapi.task.dao.TaskDetailDao;
import com.gogoyang.rpgapi.user.dao.UserDao;
import com.gogoyang.rpgapi.user.entity.User;
import com.gogoyang.rpgapi.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class JobService implements IJobService {

    private final JobDao jobDao;
    private final UserDao userDao;
    private final TaskDetailDao taskDetailDao;

    @Autowired
    public JobService(JobDao jobDao, UserDao userDao, TaskDetailDao taskDetailDao) {
        this.jobDao = jobDao;
        this.userDao = userDao;
        this.taskDetailDao = taskDetailDao;
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
}

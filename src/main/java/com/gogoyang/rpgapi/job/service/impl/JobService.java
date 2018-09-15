package com.gogoyang.rpgapi.job.service.impl;

import com.gogoyang.rpgapi.job.dao.JobDao;
import com.gogoyang.rpgapi.job.service.IJobService;
import com.gogoyang.rpgapi.job.vo.CreateJobRequest;
import com.gogoyang.rpgapi.job.vo.CreateJobResponse;
import com.gogoyang.rpgapi.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class JobService implements IJobService{

    private final JobDao jobDao;

    @Autowired
    public JobService(JobDao jobDao) {
        this.jobDao = jobDao;
    }

    @Override
    @Transactional
    public Response createJob(CreateJobRequest request) {
        Response response=new Response();
        CreateJobResponse jobResponse=new CreateJobResponse();
        jobResponse.setJobId(jobDao.save(request.toJob()).getJobId());
        response.setData(jobResponse);
        return response;
    }

    @Override
    public Response buildJobInfo(Integer id) {
        return null;
    }

    @Override
    public Response buildJobs(String category) {
        Response response=new Response();
        List jobs=jobDao.findAllByCategory(category);
        response.setData(jobs);
        return response;
    }
}

package com.gogoyang.rpgapi.job.service;

import com.gogoyang.rpgapi.job.entity.Job;
import com.gogoyang.rpgapi.job.vo.ApplyJobRequest;
import com.gogoyang.rpgapi.job.vo.CreateJobRequest;
import com.gogoyang.rpgapi.vo.Response;

public interface IJobService {
    Response createJob(CreateJobRequest request);

    Response buildJobInfo(Integer jobId);

    Response buildJobs(String category);

    Response applyJob(ApplyJobRequest request) throws Exception;

    Job loadJobInfoNoDetail(Integer jobId);
}

package com.gogoyang.rpgapi.job.service;

import com.gogoyang.rpgapi.job.entity.Job;
import com.gogoyang.rpgapi.job.vo.ApplyJobRequest;
import com.gogoyang.rpgapi.job.vo.CreateJobRequest;
import com.gogoyang.rpgapi.vo.Request;
import com.gogoyang.rpgapi.vo.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface IJobService {
    Response createJob(CreateJobRequest request);

    Response buildJobInfo(Integer jobId);

    Response buildJobs(Request request);

    Response applyJob(ApplyJobRequest request) throws Exception;

    Job loadJobInfoNoDetail(Integer jobId);

    Page<Job> loadJobToMatch(Request request);
}

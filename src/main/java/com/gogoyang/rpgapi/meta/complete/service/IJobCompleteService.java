package com.gogoyang.rpgapi.meta.complete.service;

import com.gogoyang.rpgapi.meta.complete.entity.JobComplete;
import com.gogoyang.rpgapi.meta.job.entity.Job;
import org.springframework.data.domain.Page;

import java.util.ArrayList;

public interface IJobCompleteService {
    void insertJobComplete(JobComplete jobComplete) throws Exception;

    Page<JobComplete> loadJobCompleteByJobId(Integer jobId, Integer pageIndex, Integer pageSize) throws Exception;

    ArrayList<JobComplete> loadMyUnReadComplete(Integer jobId, Integer userId) throws Exception;

    void updateJobComplete(JobComplete jobComplete) throws Exception;

    ArrayList<JobComplete> loadUnprocessComplete(Integer jobId) throws Exception;
}

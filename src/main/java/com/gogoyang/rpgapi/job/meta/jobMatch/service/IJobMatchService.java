package com.gogoyang.rpgapi.job.meta.jobMatch.service;

import com.gogoyang.rpgapi.job.meta.jobMatch.entity.JobMatch;

import java.util.ArrayList;

public interface IJobMatchService {
    JobMatch createJobMatch(JobMatch jobMatch) throws Exception;

    ArrayList<JobMatch> loadMyNewJobMatch(Integer userId) throws Exception;

    Integer countMatchingUsers(Integer jobId) throws Exception;

    JobMatch loadJobMatchByUserIdAndJobId(Integer userId, Integer jobId) throws Exception;

    JobMatch loadJobMatchByJobMatchId(Integer jobMatchId) throws Exception;

    ArrayList<JobMatch> loadJobMatchByJobId(Integer jobId) throws Exception;

    void updateJobMatch(JobMatch jobMatch) throws Exception;
}

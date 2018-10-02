package com.gogoyang.rpgapi.job.jobMatch.service;

import com.gogoyang.rpgapi.job.jobMatch.entity.JobMatch;

import java.util.ArrayList;

public interface IJobMatchService {
    JobMatch createJobMatch(JobMatch jobMatch) throws Exception;

    ArrayList<JobMatch> loadMyNewJobMatch(Integer userId) throws Exception;

    Integer countMatchingUsers(Integer jobId) throws Exception;

    JobMatch loadJobMatchByUserIdAndJobId(Integer userId, Integer jobId) throws Exception;
}

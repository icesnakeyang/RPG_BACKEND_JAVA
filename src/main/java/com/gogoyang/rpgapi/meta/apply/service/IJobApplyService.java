package com.gogoyang.rpgapi.meta.apply.service;

import com.gogoyang.rpgapi.meta.apply.entity.JobApply;

import java.util.ArrayList;

public interface IJobApplyService {
    ArrayList<JobApply> loadJobApplyByJobId(Integer jobId) throws Exception;

    void createJobApply(JobApply jobApply) throws Exception;

    boolean isApplied(Integer userId, Integer jobId) throws Exception;

    Integer countApplyUsers(Integer jobId) throws Exception;

    ArrayList<JobApply> loadMyApplies(Integer userId) throws Exception;

    void matchJobApply(JobApply jobApply) throws Exception;

    void updateJobApply(JobApply jobApply) throws Exception;

}

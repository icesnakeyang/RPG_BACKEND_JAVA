package com.gogoyang.rpgapi.meta.apply.service;

import com.gogoyang.rpgapi.meta.apply.entity.JobApply;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.Map;

public interface IJobApplyService {
    JobApply getJobApply(Map qIn) throws Exception;

    void insertJobApply(JobApply jobApply) throws Exception;

    /**
     * 批量查询任务申请
     * @param qIn
     * jobApplyId
     * jobId
     * status
     * processUserId
     * applyUserId
     * offset
     * size
     * @return
     */
    ArrayList<JobApply> listJobApply(Map qIn);


    boolean isApplied(Integer userId, Integer jobId) throws Exception;

    Integer countApplyUsers(String jobId) throws Exception;

    ArrayList<JobApply> listMyApplies(Integer userId) throws Exception;

    void matchJobApply(JobApply jobApply) throws Exception;

    void updateJobApply(JobApply jobApply) throws Exception;


    ArrayList<JobApply> listPartyBNewJob(Integer userId) throws Exception;
}

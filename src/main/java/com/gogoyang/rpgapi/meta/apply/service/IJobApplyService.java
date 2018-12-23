package com.gogoyang.rpgapi.meta.apply.service;

import com.gogoyang.rpgapi.meta.apply.entity.JobApply;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.Map;

public interface IJobApplyService {
    JobApply loadJobApplyByUserIdAndJobId(Integer userId, Integer jobId) throws Exception;

    void insertJobApply(JobApply jobApply) throws Exception;

    /**
     * 读取指定任务的所有未处理的申请记录
     * @param jobId
     * @return
     * @throws Exception
     */
    ArrayList<JobApply> listJobApplyByJobId(Integer jobId) throws Exception;

    /**
     * 读取任务申请列表
     * @param qIn
     * @return
     * @throws Exception
     */
    Page<JobApply> listJobApply(Map qIn) throws Exception;

    boolean isApplied(Integer userId, Integer jobId) throws Exception;

    Integer countApplyUsers(Integer jobId) throws Exception;

    ArrayList<JobApply> loadMyApplies(Integer userId) throws Exception;

    void matchJobApply(JobApply jobApply) throws Exception;

    void updateJobApply(JobApply jobApply) throws Exception;

    Page<JobApply> listJobapplybyUserId(Integer userId, Integer pageIndex, Integer pageSize) throws Exception;
}

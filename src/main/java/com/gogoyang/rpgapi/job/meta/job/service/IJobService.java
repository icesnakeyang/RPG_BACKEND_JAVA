package com.gogoyang.rpgapi.job.meta.job.service;

import com.gogoyang.rpgapi.job.meta.job.entity.Job;
import org.springframework.data.domain.Page;

import java.util.ArrayList;


public interface IJobService {
    Job createJob(Job job) throws Exception;

    Job loadJobByJobId(Integer jobId) throws Exception;

    Job loadJobByJobIdTiny(Integer jobId) throws Exception;

    /**
     * 读取没有成交的任务
     * @param pageIndex
     * @param pageSize
     * @return
     * @throws Exception
     */
    Page<Job> loadJobUnMatch(Integer pageIndex, Integer pageSize) throws Exception;

    /**
     * 读取所有用户已经申请，但还没有成交的任务
     * @param pageIndex
     * @param pageSize
     * @return
     * @throws Exception
     */
    ArrayList<Job> loadJobToMatch(Integer pageIndex, Integer pageSize) throws Exception;

    ArrayList loadMyApplyJob(Integer userId) throws Exception;

    void updateJob(Job job) throws Exception;
}

package com.gogoyang.rpgapi.meta.job.service;

import com.gogoyang.rpgapi.framework.constant.JobStatus;
import com.gogoyang.rpgapi.meta.job.entity.Job;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.Map;

public interface IJobService {
    Job insertJob(Job job) throws Exception;

    Job loadJobByJobId(Integer jobId) throws Exception;

    Job loadJobByJobIdTiny(Integer jobId) throws Exception;

    Job getJobByTaskId(Integer taskId) throws Exception;

    /**
     * 根据jobStatus读取所有job
     * load all jobs by jobStatus
     * paginate the result
     *
     * @param pageIndex
     * @param pageSize
     * @return
     * @throws Exception
     */
    Page<Job> loadJobByStatus(JobStatus jobStatus, Integer pageIndex, Integer pageSize) throws Exception;

    /**
     * 修改任务
     * @param job
     * @throws Exception
     */
    void updateJob(Job job) throws Exception;

    /**
     * 获取我是甲方的任务
     * @param userId
     * @param jobStatus
     * @param pageIndex
     * @param pageSize
     * @return
     * @throws Exception
     */
    Page<Job> loadPartyAJob(Integer userId, JobStatus jobStatus, Integer pageIndex, Integer pageSize) throws Exception;

    /**
     * 获取我是乙方的任务
     * @param userId
     * @param jobStatus
     * @param pageIndex
     * @param pageSize
     * @return
     * @throws Exception
     */
    Page<Job> loadPartyBJob(Integer userId, JobStatus jobStatus, Integer pageIndex, Integer pageSize) throws Exception;

    Page<Job> listMyPendingJob(Integer partyAId, JobStatus jobStatus, Integer pageIndex, Integer pageSize) throws Exception;

    Page<Job> loadJobByStausMap(Map qIn) throws Exception;
}

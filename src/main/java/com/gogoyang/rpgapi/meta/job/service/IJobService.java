package com.gogoyang.rpgapi.meta.job.service;

import com.gogoyang.rpgapi.framework.constant.JobStatus;
import com.gogoyang.rpgapi.meta.job.entity.Job;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.Map;

public interface IJobService {
    Job insertJob(Job job) throws Exception;

    Job getJobByJobId(Integer jobId) throws Exception;

    Job getJobByJobIdTiny(Integer jobId) throws Exception;

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
    Page<Job> listJobByStatus(JobStatus jobStatus, Integer pageIndex, Integer pageSize) throws Exception;

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
    Page<Job> listPartyAJob(Integer userId, JobStatus jobStatus, Integer pageIndex, Integer pageSize) throws Exception;

    /**
     * 获取我是乙方的任务
     * @param userId
     * @param jobStatus
     * @param pageIndex
     * @param pageSize
     * @return
     * @throws Exception
     */
    Page<Job> listPartyBJob(Integer userId, JobStatus jobStatus, Integer pageIndex, Integer pageSize) throws Exception;

    Page<Job> listMyPendingJob(Integer partyAId, JobStatus jobStatus, Integer pageIndex, Integer pageSize) throws Exception;

    Page<Job> listJobByStausMap(Map qIn) throws Exception;

    void deleteJob(Integer jobId) throws Exception;

    Page<Job> listPublicJob(Map qIn) throws Exception;
}

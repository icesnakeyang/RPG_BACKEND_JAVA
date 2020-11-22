package com.gogoyang.rpgapi.meta.job.service;

import com.gogoyang.rpgapi.framework.constant.JobStatus;
import com.gogoyang.rpgapi.meta.job.entity.Job;

import java.util.ArrayList;
import java.util.Map;

public interface IJobService {
    void insertJob(Job job) throws Exception;

    /**
     * 读取Job的详细信息，包括jobDetail
     *
     * @param jobId
     * @return
     * @throws Exception
     */
     Job getJobDetailByJobId(String jobId) throws Exception;

    /**
     * 读取job，不带detail
     *
     * @param jobId
     * @return
     * @throws Exception
     */
     Job getJobTinyByJobId(String jobId) throws Exception;

    /**
     * 根据taskId读取job
     *
     * @param taskId
     * @return
     * @throws Exception
     */
     Job getJobByTaskId(String taskId) throws Exception;

    /**
     * 根据jobStatus读取jobs
     * read jobs by jobStatus
     * paginate the list
     *
     * @param pageIndex
     * @param pageSize
     * @return
     * @throws Exception
     */
     ArrayList<Job> listJobByStatus(JobStatus jobStatus, Integer pageIndex, Integer pageSize) throws Exception;

    /**
     * 修改job
     *
     * @param job
     * @throws Exception
     */
     void updateJob(Job job) throws Exception;

    /**
     * 读取所有我是甲方的任务
     *
     * @param qIn
     * @return
     * @throws Exception
     */
     Map listPartyAJob(Map qIn) throws Exception;

    /**
     * 读取我是乙方的所有任务
     *
     * @param qIn
     * @return
     * @throws Exception
     */
     Map listPartyBJob(Map qIn) throws Exception;

     Map listMyPendingJob(String partyAId,
                                           Integer pageIndex, Integer pageSize) throws Exception;

     void deleteJob(String jobId) throws Exception;

     Map listPublicJob(Integer pageIndex, Integer pageSize) throws Exception;

    Map listMyPartyAAcceptJob(String userId, Integer pageIndex, Integer pageSize) throws Exception;

     ArrayList<Job> listMyPartyBAcceptJob(String userId, Integer pageIndex, Integer pageSize) throws Exception;
}

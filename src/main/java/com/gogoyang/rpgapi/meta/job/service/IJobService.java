package com.gogoyang.rpgapi.meta.job.service;

import com.gogoyang.rpgapi.framework.constant.JobStatus;
import com.gogoyang.rpgapi.meta.job.entity.Job;

import java.util.ArrayList;

public interface IJobService {
    public void insertJob(Job job) throws Exception;

    /**
     * 读取Job的详细信息，包括jobDetail
     *
     * @param jobId
     * @return
     * @throws Exception
     */
    public Job getJobDetailByJobId(String jobId) throws Exception;

    /**
     * 读取job，不带detail
     *
     * @param jobId
     * @return
     * @throws Exception
     */
    public Job getJobTinyByJobId(String jobId) throws Exception;

    /**
     * 根据taskId读取job
     *
     * @param taskId
     * @return
     * @throws Exception
     */
    public Job getJobByTaskId(String taskId) throws Exception;

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
    public ArrayList<Job> listJobByStatus(JobStatus jobStatus, Integer pageIndex, Integer pageSize) throws Exception;

    /**
     * 修改job
     *
     * @param job
     * @throws Exception
     */
    public void updateJob(Job job) throws Exception;

    /**
     * 读取所有我是甲方的任务
     *
     * @param userId
     * @param jobStatus
     * @param pageIndex
     * @param pageSize
     * @return
     * @throws Exception
     */
    public ArrayList<Job> listPartyAJob(String userId, JobStatus jobStatus, Integer pageIndex, Integer pageSize) throws Exception;

    /**
     * 读取我是乙方的所有任务
     *
     * @param userId
     * @param jobStatus
     * @param pageIndex
     * @param pageSize
     * @return
     * @throws Exception
     */
    public ArrayList<Job> listPartyBJob(Integer userId, JobStatus jobStatus,
                                        Integer pageIndex, Integer pageSize) throws Exception;

    public ArrayList<Job> listMyPendingJob(String partyAId,
                                           Integer pageIndex, Integer pageSize) throws Exception;

    public void deleteJob(String jobId) throws Exception;

    public ArrayList<Job> listPublicJob(Integer pageIndex, Integer pageSize) throws Exception;

    public ArrayList<Job> listMyPartyAAcceptJob(String userId, Integer pageIndex, Integer pageSize) throws Exception;

    public ArrayList<Job> listMyPartyBAcceptJob(String userId, Integer pageIndex, Integer pageSize) throws Exception;
}

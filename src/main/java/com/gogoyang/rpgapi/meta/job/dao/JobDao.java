package com.gogoyang.rpgapi.meta.job.dao;

import com.gogoyang.rpgapi.meta.job.entity.Job;
import com.gogoyang.rpgapi.meta.job.entity.JobDetail;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.Map;

@Mapper
public interface JobDao {
    ArrayList<Job> listMyPendingJob(Map qIn);

    ////////////////////////////

    /**
     * 读取任务简要信息
     * @param qIn
     * jobId
     * taskId
     * @return
     */
    Job getJob(Map qIn);

    /**
     * 读取任务详情
     * @param jobId
     * @return
     */
    JobDetail getJobDetail(String jobId);

    /**
     * 批量查询job任务
     * @param qIn
     * status或statusList
     * partyAId
     * partyBId
     * offset
     * size
     * @return
     */
    ArrayList<Job> listJob(Map qIn);

    void createJob(Job job);

    void createJobDetail(JobDetail jobDetail);

    void updateJobTiny(Job job);

    void updateJobDetail(JobDetail jobDetail);

    void deleteJob(String jobId);

    void deleteJobDetail(String jobId);
}

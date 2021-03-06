package com.gogoyang.rpgapi.meta.complete.dao;

import com.gogoyang.rpgapi.meta.complete.entity.JobComplete;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.Map;

@Mapper
public interface JobCompleteDao {

    void createJobComplete(JobComplete jobComplete);

    JobComplete getJobComplete(Map qIn);

    /**
     *
     * @param qIn
     * jobId
     * status
     * createUserUnread
     * createdUserId
     * processUserUnread
     * processUserId
     * offset
     * @return
     */
    ArrayList<JobComplete> listJobComplete(Map qIn);

    /**
     * 统计所有未阅读的任务完成日志
     *
     * @param qIn
     * userId
     * jobId
     * @return
     */
    Integer totalUnreadComplete(Map qIn);

    /**
     * 把所有未阅读的验收日志设置为当前阅读时间
     * @param qIn
     * userId
     * readTime
     * jobId
     */
    void setJobCompleteReadTime(Map qIn);

    /**
     * 设置甲方对处理结果的阅读时间
     * @param qIn
     * jobId
     * processReadTime
     * userId
     */
    void setJobCompleteProcessReadTime(Map qIn);

    void updateJobComplete(JobComplete jobComplete);

    /**
     * 统计任务的完成验收日志数量
     * @param qIn
     * jobId
     * @return
     */
    Integer totalJobComplete(Map qIn);
}

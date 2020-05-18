package com.gogoyang.rpgapi.meta.log.dao;

import com.gogoyang.rpgapi.meta.log.entity.JobLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.Map;

@Mapper
public interface JobLogDao {

    void createJobLog(JobLog jobLog);

    /**
     * 读取任务日志详情
     * @param jobLogId
     * @return
     */
    JobLog getJobLog(String jobLogId);

    /**
     * 批量查询任务日志
     * @param qIn
     * jobId
     * createdUserId
     * unread
     * userId 如果要查询用户未读日志，unread=true, 并且指定userId为当前登录用户
     * partyAId
     * partyBId
     * offset
     * size
     * @return
     */
    ArrayList<JobLog> listJobLog(Map qIn);

    /**
     * 统计所有未阅读的任务日志
     *
     * @param qIn
     * jobId:选填，指定任务
     * userId：必填，查询readTime为null时，createdUserId不是userId
     * partyAId：选填，查询甲方未读
     * partyBId：选填，查询乙方未读
     * @return
     */
    Integer totalUnreadLog(Map qIn);

    /**
     * 设置当前用户所有未读的任务日志的阅读时间为当前时间
     * @param qIn
     * readTime：必填
     * userId：必填
     * jobId：必填
     */
    void setJobLogReadTime(Map qIn);

    /**
     * 统计任务日志总数
     * @param qIn
     * jobId
     * @return
     */
    Integer totalJobLog(Map qIn);
}

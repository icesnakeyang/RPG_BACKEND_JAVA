package com.gogoyang.rpgapi.meta.log.service;

import com.gogoyang.rpgapi.meta.log.entity.JobLog;

import java.util.ArrayList;
import java.util.Map;


public interface IJobLogService {
    void createJobLog(JobLog jobLog) throws Exception;

    /**
     * 读取任务日志详情
     * @param jobLogId
     * @return
     */
    JobLog getJobLog(String jobLogId) throws Exception;

    /**
     * 批量查询任务日志
     * @param qIn
     * jobId
     * createdUserId
     * unread
     * partyAId
     * partyBId
     * offset
     * size
     * @return
     */
    ArrayList<JobLog> listJobLog(Map qIn) throws Exception;

    ArrayList<JobLog> loadJobLogByJobId(String jobLogId, Integer pageIndex, Integer pageSize) throws Exception;

    ArrayList<JobLog> listPartyAUnreadJobLog(String userId) throws Exception;

    ArrayList<JobLog> listPartyAUnreadJobLogJobId(String userId, String jobId) throws Exception;
    ArrayList<JobLog> listPartyBUnreadJobLog(String userId) throws Exception;
    ArrayList<JobLog> listPartyBUnreadJobLogJobId(String userId, String jobId) throws Exception;

    /**
     * 统计所有未阅读的任务日志
     * @param qIn
     * jobId:选填，指定任务
     * userId：必填，查询readTime为null时，createdUserId不是userId
     * partyAId：选填，查询甲方未读
     * partyBId：选填，查询乙方未读
     * @return
     * @throws Exception
     */
    Integer totalUnreadLog(Map qIn) throws Exception;

    /**
     * 设置阅读时间
     * readTime：必填
     * userId：必填
     * jobId：必填
     * @param qIn
     * @throws Exception
     */
    void setJobLogReadTime(Map qIn) throws Exception;

    /**
     * 统计任务日志总数
     * @param qIn
     * jobId
     * @return
     */
    Integer totalJobLog(Map qIn);


    void deleteJobLog(String jobLogId) throws Exception;

    /**
     * 修改日志内容
     * @param jobLog
     * @throws Exception
     */
    void updateJobLog(JobLog jobLog) throws Exception;
}

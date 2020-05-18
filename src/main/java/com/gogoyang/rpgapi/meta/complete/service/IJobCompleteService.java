package com.gogoyang.rpgapi.meta.complete.service;

import com.gogoyang.rpgapi.framework.constant.LogStatus;
import com.gogoyang.rpgapi.meta.complete.entity.JobComplete;

import java.util.ArrayList;
import java.util.Map;

public interface IJobCompleteService {
    /**
     * 创建一个验收日志
     * @param jobComplete
     * @throws Exception
     */
    void insertJobComplete(JobComplete jobComplete) throws Exception;

    ArrayList<JobComplete> listJobComplete(Map qIn) throws Exception;

    /**
     * 获取一个任务的所有验收日志
     * @param jobId
     * @param pageIndex
     * @param pageSize
     * @return
     * @throws Exception
     */
    ArrayList<JobComplete> loadJobCompleteByJobId(String jobId, Integer pageIndex, Integer pageSize) throws Exception;

    /**
     * 甲方读取未阅读的验收日志
     */
    ArrayList<JobComplete> listPartyAUnread(String userId) throws Exception;

    /**
     * 甲方读取某个任务的未阅读的验收日志
     */
    ArrayList<JobComplete> listPartyAUnreadJobId(String userId, String jobId) throws Exception;

    /**
     * read all un read complete process by me
     */
    ArrayList<JobComplete> listPartyBUnread(String userId) throws Exception;

    ArrayList<JobComplete> listPartyBUnreadAccept(String userId) throws Exception;

    ArrayList<JobComplete> listPartyBUnreadJobId(String userId, String jobId) throws Exception;

    /**
     * 修改验收日志/处理验收日志申请
     * @param jobComplete
     * @throws Exception
     */
    void updateJobComplete(JobComplete jobComplete) throws Exception;

    /**
     * 获取所有未处理的验收日志申请
     * @param jobId
     * @return
     * @throws Exception
     */
    JobComplete getUnprocessComplete(String jobId) throws Exception;

    /**
     * 读取验收成功的验收日志
     * @param jobId
     * @return
     * @throws Exception
     */
    JobComplete getCompleteByStatus(String jobId, LogStatus logStatus) throws Exception;

    /**
     * 统计所有未阅读的任务完成日志
     * @param in
     * userId
     * jobId
     * @return
     * @throws Exception
     */
    Integer totalUnreadComplete(Map in) throws Exception;

    void setJobCompleteReadTime(Map qIn) throws Exception;

    /**
     *
     * @param qIn
     * jobId
     * processReadTime
     * userId
     * @throws Exception
     */
    void setJobCompleteProcessReadTime(Map qIn) throws Exception;

    /**
     * 统计任务的完成验收日志数量
     * @param qIn
     * jobId
     * @return
     */
    Integer totalJobComplete(Map qIn);
}

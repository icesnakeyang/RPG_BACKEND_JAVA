package com.gogoyang.rpgapi.meta.complete.service;

import com.gogoyang.rpgapi.framework.constant.LogStatus;
import com.gogoyang.rpgapi.meta.complete.entity.JobComplete;
import org.springframework.data.domain.Page;

import java.util.ArrayList;

public interface IJobCompleteService {
    /**
     * 创建一个验收日志
     * @param jobComplete
     * @throws Exception
     */
    void insertJobComplete(JobComplete jobComplete) throws Exception;

    /**
     * 获取一个任务的所有验收日志
     * @param jobId
     * @param pageIndex
     * @param pageSize
     * @return
     * @throws Exception
     */
    Page<JobComplete> loadJobCompleteByJobId(Integer jobId, Integer pageIndex, Integer pageSize) throws Exception;

    /**
     * 甲方读取未阅读的验收日志
     */
    ArrayList<JobComplete> listPartyAUnread(Integer userId) throws Exception;

    /**
     * 甲方读取某个任务的未阅读的验收日志
     */
    ArrayList<JobComplete> listPartyAUnreadJobId(Integer userId, Integer jobId) throws Exception;

    /**
     * read all un read complete process by me
     */
    ArrayList<JobComplete> listPartyBUnread(Integer userId) throws Exception;

    ArrayList<JobComplete> listPartyBUnreadAccept(Integer userId) throws Exception;

    ArrayList<JobComplete> listPartyBUnreadJobId(Integer userId, Integer jobId) throws Exception;

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
    JobComplete getUnprocessComplete(Integer jobId) throws Exception;

    /**
     * 读取验收成功的验收日志
     * @param jobId
     * @return
     * @throws Exception
     */
    JobComplete getCompleteByStatus(Integer jobId, LogStatus logStatus) throws Exception;
}

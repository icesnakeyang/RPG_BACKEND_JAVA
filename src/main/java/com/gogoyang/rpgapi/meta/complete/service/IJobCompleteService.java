package com.gogoyang.rpgapi.meta.complete.service;

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
     * 获取一个任务，一个用户的所有未读验收日志
     * @param jobId
     * @param userId
     * @return
     * @throws Exception
     */
    ArrayList<JobComplete> loadMyUnReadComplete(Integer jobId, Integer userId) throws Exception;

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
    ArrayList<JobComplete> loadUnprocessComplete(Integer jobId) throws Exception;
}

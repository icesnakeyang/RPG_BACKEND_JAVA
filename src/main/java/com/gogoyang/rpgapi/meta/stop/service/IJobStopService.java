package com.gogoyang.rpgapi.meta.stop.service;

import com.gogoyang.rpgapi.meta.stop.entity.JobStop;

import java.util.ArrayList;
import java.util.Map;

public interface IJobStopService {
    /**
     * 新增
     * @param jobStop
     * @throws Exception
     */
    void insertJobStop(JobStop jobStop) throws Exception;

    /**
     * 读取所有
     * @param jobId
     * @param pageIndex
     * @param pageSize
     * @return
     * @throws Exception
     */
    ArrayList<JobStop> loadJobStopByJobId(String jobId, Integer pageIndex, Integer pageSize) throws Exception;

    /**
     * 获取未处理的终止申请
     * @param jobId
     * @return
     * @throws Exception
     */
    JobStop loadJobStopUnProcess(String jobId) throws Exception;

    /**
     * 读取未读
     * @param jobId
     * @param userId
     * @return
     * @throws Exception
     */
    ArrayList<JobStop> loadMyUnReadStop(String jobId, String userId) throws Exception;

    /**
     * 修改
     * @param jobStop
     * @throws Exception
     */
    void updateJobStop(JobStop jobStop) throws Exception;

    /**
     * 统计所有未阅读的任务终止日志
     * @param qIn
     * userId
     * jobId
     * @return
     */
    Integer totalUnreadStop(Map qIn);

    /**
     * 统计一个任务的终止日志数
     * @param qIn
     * jobId
     * @return
     */
    Integer totalStop(Map qIn);
}

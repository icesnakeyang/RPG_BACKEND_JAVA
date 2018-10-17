package com.gogoyang.rpgapi.meta.stop.service;

import com.gogoyang.rpgapi.meta.stop.entity.JobStop;
import org.springframework.data.domain.Page;

import java.util.ArrayList;

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
    Page<JobStop> loadJobStopByJobId(Integer jobId, Integer pageIndex, Integer pageSize) throws Exception;

    /**
     * 获取未处理的终止申请
     * @param jobId
     * @return
     * @throws Exception
     */
    JobStop loadJobStopUnProcess(Integer jobId) throws Exception;

    /**
     * 读取未读
     * @param jobId
     * @param userId
     * @return
     * @throws Exception
     */
    ArrayList<JobStop> loadMyUnReadStop(Integer jobId, Integer userId) throws Exception;

    /**
     * 修改
     * @param jobStop
     * @throws Exception
     */
    void updateJobStop(JobStop jobStop) throws Exception;
}

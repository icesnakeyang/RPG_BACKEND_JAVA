package com.gogoyang.rpgapi.business.job.myJob.myLog.service;

import com.gogoyang.rpgapi.meta.log.entity.JobLog;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface IMyLogBusinessService {
    /**
     * 创建一个任务日志
     * @param in
     * @throws Exception
     */
    void createLog(Map in) throws Exception;

    /**
     * 读取所有任务日志
     * @param in
     * @return
     * @throws Exception
     */
    Page<JobLog> loadJobLog(Map in) throws Exception;

    void setJobLogReadTime(Map in)throws Exception;

    /**
     * 统计未阅读的日志数量
     * @param in
     * @return
     * @throws Exception
     */
    Integer countUnreadJobLog(Map in) throws Exception;
}

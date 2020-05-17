package com.gogoyang.rpgapi.business.job.myJob.log.service;

import com.gogoyang.rpgapi.meta.log.entity.JobLog;

import java.util.ArrayList;
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
    ArrayList<JobLog> loadJobLog(Map in) throws Exception;

    void setJobLogReadTime(Map in)throws Exception;
}

package com.gogoyang.rpgapi.meta.log.service;

import com.gogoyang.rpgapi.meta.log.entity.JobLog;
import org.springframework.data.domain.Page;

import java.util.ArrayList;


public interface IJobLogService {
    void createJobLog(JobLog jobLog) throws Exception;
    Page<JobLog> loadJobLogByJobId(Integer jobLogId, Integer pageIndex, Integer pageSize) throws Exception;
    void updateJobLog(JobLog jobLog) throws Exception;
    ArrayList<JobLog> loadMyUnreadJobLog(Integer jobId, Integer userId) throws Exception;
}

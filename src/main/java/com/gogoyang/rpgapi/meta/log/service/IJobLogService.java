package com.gogoyang.rpgapi.meta.log.service;

import com.gogoyang.rpgapi.meta.log.entity.JobLog;


public interface IJobLogService {
    void createJobLog(JobLog jobLog) throws Exception;
    JobLog loadJobLogByJobLogId(Integer jobLogId) throws Exception;
    void updateJobLog(JobLog jobLog) throws Exception;
    Integer countUnreadJobLog(Integer jobId, Integer userId) throws Exception;
}

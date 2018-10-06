package com.gogoyang.rpgapi.job.business.log;

import com.gogoyang.rpgapi.job.meta.jobLog.entity.JobLog;

import java.util.ArrayList;
import java.util.Map;

public interface ILogService {
    void createJobLog(Map in) throws Exception;

    ArrayList<JobLog> loadJobLog(Map in) throws Exception;

    void setJobLogReadTime(Map in) throws Exception;
}

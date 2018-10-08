package com.gogoyang.rpgapi.business.job.log;

import com.gogoyang.rpgapi.meta.log.entity.JobLog;

import java.util.ArrayList;
import java.util.Map;

public interface ILogService {
    void createJobLog(Map in) throws Exception;

    ArrayList<JobLog> loadJobLog(Map in) throws Exception;

    void setJobLogReadTime(Map in) throws Exception;
}

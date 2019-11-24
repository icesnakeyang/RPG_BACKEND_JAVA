package com.gogoyang.rpgapi.meta.log.service;

import com.gogoyang.rpgapi.meta.log.entity.JobLog;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.Map;


public interface IJobLogService {
    void createJobLog(JobLog jobLog) throws Exception;
    Page<JobLog> loadJobLogByJobId(Integer jobLogId, Integer pageIndex, Integer pageSize) throws Exception;
    void updateJobLog(JobLog jobLog) throws Exception;
    ArrayList<JobLog> listPartyAUnreadJobLog(Integer userId) throws Exception;
    ArrayList<JobLog> listPartyAUnreadJobLogJobId(Integer userId, Integer jobId) throws Exception;
    ArrayList<JobLog> listPartyBUnreadJobLog(Integer userId) throws Exception;
    ArrayList<JobLog> listPartyBUnreadJobLogJobId(Integer userId, Integer jobId) throws Exception;

    Integer totalUnreadLog(Map qIn) throws Exception;

    void setJobLogReadTime(Map qIn) throws Exception;
}

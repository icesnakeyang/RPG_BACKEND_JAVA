package com.gogoyang.rpgapi.business.job.myJob.common.service;

import java.util.Map;

public interface IMyJobCommonBusinessService {
    Map totalMyUnread(Map in) throws Exception;
    Integer totalUnreadOneJob(String token, Integer jobId) throws Exception;
    Map getJobTinyByTaskId(Map in) throws Exception;
    Map getJobTinyByJobId(Map in) throws Exception;
}

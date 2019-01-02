package com.gogoyang.rpgapi.business.job.myJob.common.service;

import java.util.Map;

public interface IMyJobCommonBusinessService {
    Map loadUnreadByJobId(Map in) throws Exception;
    Map getJobTinyByTaskId(Map in) throws Exception;
}

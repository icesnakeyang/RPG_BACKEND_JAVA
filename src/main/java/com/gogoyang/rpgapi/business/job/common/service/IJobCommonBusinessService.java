package com.gogoyang.rpgapi.business.job.common.service;

import java.util.Map;

public interface IJobCommonBusinessService {
    void publishNewJob(Map in) throws Exception;

    Map listPublicJob(Map in) throws Exception;

    Map getJobDetail(Map in) throws Exception;

    Map getJobTiny(Map in) throws Exception;
}

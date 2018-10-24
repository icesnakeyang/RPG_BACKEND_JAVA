package com.gogoyang.rpgapi.business.job.detail.service;

import java.util.Map;

public interface IJobDetailBusinessService {
    Map loadJobDetail(Map in) throws Exception;

    Map loadJobTiny(Map in) throws Exception;

    Map loadUnreadByJobId(Map in) throws Exception;

    Map getJobTinyByTaskId(Map in) throws Exception;
}

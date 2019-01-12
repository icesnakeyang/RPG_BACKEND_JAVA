package com.gogoyang.rpgapi.business.job.myJob.pending.service;

import java.util.Map;

public interface IMyPendingBusinessService {
    Map listMyPendingJob(Map in) throws Exception;

    void updateJob(Map in) throws Exception;

    void deletePendingJob(Map in) throws Exception;

    Map getMyPendingJob(Map in) throws Exception;
}

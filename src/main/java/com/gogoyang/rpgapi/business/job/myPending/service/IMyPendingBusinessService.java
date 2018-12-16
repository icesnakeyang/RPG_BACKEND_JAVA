package com.gogoyang.rpgapi.business.job.myPending.service;

import java.util.Map;

public interface IMyPendingBusinessService {
    Map listMyPendingJob(Map in) throws Exception;

    void updateJob(Map in) throws Exception;

    void deletePendingJob(Map in) throws Exception;
}

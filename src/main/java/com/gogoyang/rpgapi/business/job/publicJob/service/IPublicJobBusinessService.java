package com.gogoyang.rpgapi.business.job.publicJob.service;

import java.util.Map;

public interface IPublicJobBusinessService {
    Map listPublicJob(Map in) throws Exception;
    Map getJobDetail(Map in) throws Exception;
}

package com.gogoyang.rpgapi.business.job.myJob.apply.service;

import java.util.Map;

public interface IMyApplyBusinessService {
    Map loadJobByMyApply(Map in) throws Exception;
    void applyJob(Map in) throws Exception;
}

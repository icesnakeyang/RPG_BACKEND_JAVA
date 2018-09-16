package com.gogoyang.rpgapi.job.service;

import com.gogoyang.rpgapi.job.entity.JobApplyLog;

public interface IJobApplyLogService {
    boolean isApplied(Integer userId);
}

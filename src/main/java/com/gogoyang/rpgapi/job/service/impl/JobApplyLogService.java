package com.gogoyang.rpgapi.job.service.impl;

import com.gogoyang.rpgapi.job.dao.JobApplyLogDao;
import com.gogoyang.rpgapi.job.entity.JobApplyLog;
import com.gogoyang.rpgapi.job.service.IJobApplyLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JobApplyLogService implements IJobApplyLogService {
    private final JobApplyLogDao jobApplyLogDao;

    @Autowired
    public JobApplyLogService(JobApplyLogDao jobApplyLogDao) {
        this.jobApplyLogDao = jobApplyLogDao;
    }

    @Override
    public boolean isApplied(Integer userId) {
        List<JobApplyLog> jobApplyLog = jobApplyLogDao.findByApplyUserIdAndResultIsNull(userId);
        if (jobApplyLog != null) {
            if (jobApplyLog.size() > 0) {
                //the job has applied by current user already.
                return true;
            }
        }
        return false;
    }
}

package com.gogoyang.rpgapi.meta.log.service;

import com.gogoyang.rpgapi.meta.log.dao.JobLogDao;
import com.gogoyang.rpgapi.meta.log.entity.JobLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;

@Service
public class JobLogService implements IJobLogService{
    private final JobLogDao jobLogDao;

    @Autowired
    public JobLogService(JobLogDao jobLogDao) {
        this.jobLogDao = jobLogDao;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void createJobLog(JobLog jobLog) throws Exception {
        if(jobLog.getJobLogId()!=null){
            throw new Exception("10051");
        }
        jobLogDao.save(jobLog);
    }

    @Override
    public JobLog loadJobLogByJobLogId(Integer jobLogId) throws Exception {
        JobLog jobLog=jobLogDao.
        return null;
    }

    @Override
    public void updateJobLog(JobLog jobLog) throws Exception {

    }

    @Override
    public Integer countUnreadJobLog(Integer jobId, Integer userId) throws Exception {
        ArrayList<JobLog> jobLogs=jobLogDao.findAllByReadTimeIsNullAndCreatedUserIdIsNot(userId);
        return null;
    }
}

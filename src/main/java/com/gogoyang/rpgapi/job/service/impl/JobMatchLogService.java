package com.gogoyang.rpgapi.job.service.impl;

import com.gogoyang.rpgapi.job.dao.JobMatchLogDao;
import com.gogoyang.rpgapi.job.entity.JobMatchLog;
import com.gogoyang.rpgapi.job.service.IJobMatchLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Map;

@Service
public class JobMatchLogService implements IJobMatchLogService{
    private final JobMatchLogDao jobMatchLogDao;

    @Autowired
    public JobMatchLogService(JobMatchLogDao jobMatchLogDao) {
        this.jobMatchLogDao = jobMatchLogDao;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void addNewMatchJobLog(Map map) throws Exception {
        /**
         * map {jobId, matchUserId}
         * 首先，查询matchJobLog, jobId, userId，processResult==null,
         * 如果有记录即说明，已经保存了该用户对该任务的分配
         * 如果没有记录，增加新的记录。
         *
         */
        Integer jobId=(Integer)map.get("jobId");
        Integer userId=(Integer)map.get("matchUserId");

        JobMatchLog jobMatchLog=jobMatchLogDao.findByJobIdAndMatchUserIdAndProcessResultIsNull(jobId, userId);
        if(jobMatchLog!=null){
            throw new Exception("error 323094");
        }
        jobMatchLog=new JobMatchLog();
        jobMatchLog.setJobId(jobId);
        jobMatchLog.setMatchUserId(userId);
        jobMatchLog.setMatchTime(new Date());
        jobMatchLogDao.save(jobMatchLog);
    }
}

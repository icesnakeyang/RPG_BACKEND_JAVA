package com.gogoyang.rpgapi.meta.log.service;

import com.gogoyang.rpgapi.meta.log.dao.JobLogDao;
import com.gogoyang.rpgapi.meta.log.dao.JobLogMapper;
import com.gogoyang.rpgapi.meta.log.entity.JobLog;
import com.gogoyang.rpgapi.meta.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Map;

@Service
public class JobLogService implements IJobLogService {
    private final JobLogDao jobLogDao;
    private final IUserService iUserService;
    private final JobLogMapper jobLogMapper;

    @Autowired
    public JobLogService(JobLogDao jobLogDao,
                         IUserService iUserService,
                         JobLogMapper jobLogMapper) {
        this.jobLogDao = jobLogDao;
        this.iUserService = iUserService;
        this.jobLogMapper = jobLogMapper;
    }

    /**
     * 创建一个任务日志
     *
     * @param jobLog
     * @throws Exception
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public void createJobLog(JobLog jobLog) throws Exception {
        if (jobLog.getJobLogId() != null) {
            throw new Exception("10051");
        }
        jobLogDao.save(jobLog);
    }

    /**
     * 读取一个任务的所有日志
     *
     * @param jobId
     * @return
     * @throws Exception
     */
    @Override
    public Page<JobLog> loadJobLogByJobId(Integer jobId, Integer pageIndex, Integer pageSize) throws Exception {
        Sort sort = new Sort(Sort.Direction.DESC, "jobLogId");
        Pageable pageable = new PageRequest(pageIndex, pageSize, sort);
        Page<JobLog> jobLogs = jobLogDao.findAllByJobId(jobId, pageable);
        for (int i = 0; i < jobLogs.getContent().size(); i++) {
            User user = iUserService.getUserByUserId(jobLogs.getContent().get(i).getCreatedUserId());
            if (user.getRealName() != null) {
                jobLogs.getContent().get(i).setCreatedUserName(user.getRealName());
            } else {
                jobLogs.getContent().get(i).setCreatedUserName(user.getEmail());
            }
        }
        return jobLogs;
    }

    /**
     * 修改日志
     *
     * @param jobLog
     * @throws Exception
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public void updateJobLog(JobLog jobLog) throws Exception {
        if (jobLog.getJobLogId() == null) {
            throw new Exception("10053");
        }
        jobLogDao.save(jobLog);
    }

    /**
     * 读取一个任务里所有甲方未阅读的日志
     */
    @Override
    public ArrayList<JobLog> listPartyAUnreadJobLog(Integer userId) throws Exception {
        ArrayList<JobLog> jobLogs = jobLogDao.findAllByReadTimeIsNullAndCreatedUserIdIsNotAndPartyAId(userId, userId);
        return jobLogs;
    }

    @Override
    public ArrayList<JobLog> listPartyAUnreadJobLogJobId(Integer userId, Integer jobId) throws Exception {
        ArrayList<JobLog> jobLogs = jobLogDao.findAllByReadTimeIsNullAndCreatedUserIdIsNotAndPartyAIdAndJobId(userId, userId, jobId);
        return jobLogs;
    }

    /**
     * 读取一个任务里所有乙方未阅读的日志
     */
    @Override
    public ArrayList<JobLog> listPartyBUnreadJobLog(Integer userId) throws Exception {
        ArrayList<JobLog> jobLogs = jobLogDao.findAllByReadTimeIsNullAndCreatedUserIdIsNotAndPartyBId(userId, userId);
        return jobLogs;
    }

    @Override
    public ArrayList<JobLog> listPartyBUnreadJobLogJobId(Integer userId, Integer jobId) throws Exception {
        ArrayList<JobLog> jobLogs = jobLogDao.findAllByReadTimeIsNullAndCreatedUserIdIsNotAndPartyBIdAndJobId(userId, userId, jobId);
        return jobLogs;
    }

    @Override
    public Integer totalUnreadLog(Map qIn) throws Exception {
        Integer jobId = (Integer) qIn.get("jobId");
        Integer userId = (Integer) qIn.get("userId");

        if (userId == null) {
            throw new Exception("10119");
        }

        Integer totalUnreadLog = jobLogMapper.totalUnreadLog(qIn);

        return totalUnreadLog;
    }

    @Override
    public void setJobLogReadTime(Map qIn) throws Exception {
        jobLogMapper.setJobLogReadTime(qIn);
    }

    @Override
    public Integer totalUnreadComplete(Map qIn) throws Exception {
        Integer totalUnreadComplete = jobLogMapper.totalUnreadLog(qIn);
        return totalUnreadComplete;
    }
}

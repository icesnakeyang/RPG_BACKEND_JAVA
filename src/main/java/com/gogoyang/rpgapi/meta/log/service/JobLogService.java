package com.gogoyang.rpgapi.meta.log.service;

import com.gogoyang.rpgapi.meta.log.dao.JobLogDao;
import com.gogoyang.rpgapi.meta.log.entity.JobLog;
import com.gogoyang.rpgapi.meta.user.userInfo.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;

@Service
public class JobLogService implements IJobLogService{
    private final JobLogDao jobLogDao;
    private final IUserInfoService iUserInfoService;

    @Autowired
    public JobLogService(JobLogDao jobLogDao, IUserInfoService iUserInfoService) {
        this.jobLogDao = jobLogDao;
        this.iUserInfoService = iUserInfoService;
    }

    /**
     * 创建一个任务日志
     * @param jobLog
     * @throws Exception
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public void createJobLog(JobLog jobLog) throws Exception {
        if(jobLog.getJobLogId()!=null){
            throw new Exception("10051");
        }
        jobLogDao.save(jobLog);
    }

    /**
     * 读取一个任务的所有日志
     * @param jobId
     * @return
     * @throws Exception
     */
    @Override
    public Page<JobLog> loadJobLogByJobId(Integer jobId, Integer pageIndex, Integer pageSize) throws Exception {
        Sort sort = new Sort(Sort.Direction.DESC, "jobLogId");
        Pageable pageable = new PageRequest(pageIndex, pageSize, sort);
        Page<JobLog> jobLogs = jobLogDao.findAllByJobId(jobId, pageable);
        for(int i=0;i<jobLogs.getContent().size();i++){
            jobLogs.getContent().get(i).setCreatedUserName(iUserInfoService.getUserName(
                    jobLogs.getContent().get(i).getCreatedUserId()));
        }
        return jobLogs;
    }

    /**
     * 修改日志
     * @param jobLog
     * @throws Exception
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public void updateJobLog(JobLog jobLog) throws Exception {
        if(jobLog.getJobLogId()==null){
            throw new Exception("10053");
        }
        jobLogDao.save(jobLog);
    }

    /**
     * 读取一个任务里所有我未阅读的日志
     * @param jobId
     * @param userId
     * @return
     * @throws Exception
     */
    @Override
    public ArrayList<JobLog> loadMyUnreadJobLog(Integer jobId, Integer userId) throws Exception {
        ArrayList<JobLog> jobLogs=jobLogDao.findAllByReadTimeIsNullAndCreatedUserIdIsNotAndJobId(userId, jobId);
        return jobLogs;
    }
}

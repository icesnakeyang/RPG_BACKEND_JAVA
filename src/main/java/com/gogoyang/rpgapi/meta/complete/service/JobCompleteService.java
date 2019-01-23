package com.gogoyang.rpgapi.meta.complete.service;

import com.gogoyang.rpgapi.framework.constant.JobStatus;
import com.gogoyang.rpgapi.framework.constant.LogStatus;
import com.gogoyang.rpgapi.meta.complete.dao.JobCompleteDao;
import com.gogoyang.rpgapi.meta.complete.entity.JobComplete;
import com.gogoyang.rpgapi.meta.user.entity.User;
import com.gogoyang.rpgapi.meta.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;

@Service
public class JobCompleteService implements IJobCompleteService {
    private final JobCompleteDao jobCompleteDao;
    private final IUserService iUserService;

    @Autowired
    public JobCompleteService(JobCompleteDao jobCompleteDao,
                              IUserService iUserService) {
        this.jobCompleteDao = jobCompleteDao;
        this.iUserService = iUserService;
    }

    /**
     * 创建验收任务
     *
     * @param jobComplete
     * @throws Exception
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public void insertJobComplete(JobComplete jobComplete) throws Exception {
        if (jobComplete.getCompleteId() != null) {
            throw new Exception("10057");
        }
        jobCompleteDao.save(jobComplete);
    }

    /**
     * 读取一个任务的所有验收任务
     *
     * @param jobId
     * @param pageIndex
     * @param pageSize
     * @return
     * @throws Exception
     */
    @Override
    public Page<JobComplete> loadJobCompleteByJobId(Integer jobId, Integer pageIndex, Integer pageSize) throws Exception {
        Sort sort = new Sort(Sort.Direction.DESC, "completeId");
        Pageable pageable = new PageRequest(pageIndex, pageSize, sort);
        Page<JobComplete> jobCompletes = jobCompleteDao.findAllByJobId(jobId, pageable);
        for (int i = 0; i < jobCompletes.getContent().size(); i++) {
            User user = iUserService.getUserByUserId(jobCompletes.getContent().get(i).getCreatedUserId());
            if (user.getRealName() != null) {
                jobCompletes.getContent().get(i).setCreatedUserName(user.getRealName());
            } else {
                jobCompletes.getContent().get(i).setCreatedUserName(user.getEmail());
            }
        }
        return jobCompletes;
    }

    /**
     * 读取一个任务里所有我未阅读的验收日志
     *
     * @param jobId
     * @param userId
     * @return
     * @throws Exception
     */
    @Override
    public ArrayList<JobComplete> loadMyUnReadComplete(Integer jobId, Integer userId) throws Exception {
        ArrayList<JobComplete> jobCompletes = jobCompleteDao.findAllByReadTimeIsNullAndCreatedUserIdIsNotAndJobId(userId, jobId);
        return jobCompletes;
    }

    @Override
    public ArrayList<JobComplete> listMyUnReadCompleteProcess(Integer jobId, Integer userId) throws Exception {
        ArrayList<JobComplete> jobCompleteArrayList=jobCompleteDao.findAllByProcessReadTimeIsNullAndCreatedUserIdAndJobId(userId, jobId);
        return jobCompleteArrayList;
    }


    /**
     * 修改一个验收日志
     * @param jobComplete
     * @throws Exception
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public void updateJobComplete(JobComplete jobComplete) throws Exception {
        if(jobComplete.getCompleteId()==null) {
            throw new Exception("10059");
        }
        jobCompleteDao.save(jobComplete);
    }

    /**
     *
     * @param jobId
     * @return
     * @throws Exception
     */
    @Override
    public JobComplete getUnprocessComplete(Integer jobId) throws Exception {
        JobComplete complete=jobCompleteDao.findByJobIdAndResultIsNull(jobId);
        return complete;
    }

    @Override
    public JobComplete getCompleteByStatus(Integer jobId, LogStatus logStatus) throws Exception {
        JobComplete jobComplete=jobCompleteDao.findByJobIdAndResult(jobId, logStatus);
        return jobComplete;
    }
}

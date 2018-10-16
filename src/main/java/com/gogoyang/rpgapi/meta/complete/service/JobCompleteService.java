package com.gogoyang.rpgapi.meta.complete.service;

import com.gogoyang.rpgapi.meta.complete.dao.JobCompleteDao;
import com.gogoyang.rpgapi.meta.complete.entity.JobComplete;
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
public class JobCompleteService implements IJobCompleteService {
    private final JobCompleteDao jobCompleteDao;
    private final IUserInfoService iUserInfoService;

    @Autowired
    public JobCompleteService(JobCompleteDao jobCompleteDao, IUserInfoService iUserInfoService) {
        this.jobCompleteDao = jobCompleteDao;
        this.iUserInfoService = iUserInfoService;
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
            jobCompletes.getContent().get(i).setCreatedUserName(iUserInfoService.getUserName(
                    jobCompletes.getContent().get(i).getCreatedUserId()));
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

    /**
     * 修改一个验收日志
     * @param jobComplete
     * @throws Exception
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public void updateJobComplete(JobComplete jobComplete) throws Exception {
        if(jobComplete.getCompleteId()==null){
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
    public ArrayList<JobComplete> loadUnprocessComplete(Integer jobId) throws Exception {
        ArrayList<JobComplete> completes=jobCompleteDao.findAllByJobIdAndResultIsNull(jobId);
        return completes;
    }
}

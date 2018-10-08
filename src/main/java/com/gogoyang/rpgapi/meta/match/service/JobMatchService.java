package com.gogoyang.rpgapi.meta.match.service;

import com.gogoyang.rpgapi.meta.match.dao.JobMatchDao;
import com.gogoyang.rpgapi.meta.match.entity.JobMatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;

@Service
public class JobMatchService implements IJobMatchService {
    private final JobMatchDao jobMatchDao;

    @Autowired
    public JobMatchService(JobMatchDao jobMatchDao) {
        this.jobMatchDao = jobMatchDao;
    }

    /**
     * 创建一个任务分配记录
     *
     * @param jobMatch
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public JobMatch createJobMatch(JobMatch jobMatch) throws Exception {
        /**
         * 首先，查询JobMatch, jobId, userId，processResult==null,
         * 如果有记录即说明，已经保存了该用户对该任务的分配
         * 如果没有记录，增加新的记录。
         *
         */
        JobMatch checkJobMatch = jobMatchDao.findByJobIdAndMatchUserIdAndProcessResultIsNull(
                jobMatch.getJobId(), jobMatch.getMatchUserId());
        if (checkJobMatch != null) {
            throw new Exception("10012");
        }
        jobMatch.setMatchTime(new Date());
        jobMatch = jobMatchDao.save(jobMatch);
        return jobMatch;
    }

    /**
     * 查询所有新分配给我的分配日志
     *
     * @param userId
     * @return
     * @throws Exception
     */
    @Override
    public ArrayList<JobMatch> loadMyNewJobMatch(Integer userId) throws Exception {
        ArrayList<JobMatch> newMatchs = jobMatchDao.findAllByMatchUserIdAndProcessResultIsNull(userId);
        return newMatchs;
    }

    /**
     * 统计jobId目前已分配给了多少用户。
     *
     * @param jobId
     * @return
     * @throws Exception
     */
    @Override
    public Integer countMatchingUsers(Integer jobId) throws Exception {
        ArrayList<JobMatch> matchingUsers = jobMatchDao.findAllByJobIdAndProcessResultIsNull(jobId);
        if (matchingUsers == null) {
            return 0;
        }
        return matchingUsers.size();
    }

    /**
     * 根据userId和jobId查询未处理的match记录
     *
     * @param userId
     * @param jobId
     * @return
     * @throws Exception
     */
    @Override
    public JobMatch loadJobMatchByUserIdAndJobId(Integer userId, Integer jobId) throws Exception {
        JobMatch jobMatch = jobMatchDao.findByJobIdAndMatchUserIdAndProcessResultIsNull(jobId, userId);
        return jobMatch;
    }

    @Override
    public JobMatch loadJobMatchByJobMatchId(Integer jobMatchId) throws Exception {
        JobMatch jobMatch=jobMatchDao.findByJobMatchId(jobMatchId);
        return jobMatch;
    }

    @Override
    public ArrayList<JobMatch> loadJobMatchByJobId(Integer jobId) throws Exception {
        ArrayList<JobMatch> jobMatches=jobMatchDao.findAllByJobIdAndProcessResultIsNull(jobId);
        return jobMatches;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void updateJobMatch(JobMatch jobMatch) throws Exception {
        if(jobMatch.getJobMatchId()==null){
            throw new Exception("10010");
        }
        jobMatchDao.save(jobMatch);
    }
}

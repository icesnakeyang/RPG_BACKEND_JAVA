package com.gogoyang.rpgapi.job.meta.jobMatch.dao;

import com.gogoyang.rpgapi.job.meta.jobMatch.entity.JobMatch;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface JobMatchDao extends JpaRepository<JobMatch, Integer> {
    /**
     * 查找某个任务的所有没有处理的匹配记录
     * @param jobId
     * @return
     */
    ArrayList<JobMatch> findAllByJobIdAndProcessResultIsNull(Integer jobId);

    /**
     * 查找JobMatch表，看用户userId是否已经分配了该jobId，
     * 并且，用户还未处理该分配，即ProcessResult==null
     * @param jobId
     * @param userId
     * @return
     */
    JobMatch findByJobIdAndMatchUserIdAndProcessResultIsNull(Integer jobId, Integer userId);

    /**
     * 查找userId所有新分配的任务，且未处理
     * @param userId
     * @return
     */
    ArrayList<JobMatch> findAllByMatchUserIdAndProcessResultIsNull(Integer userId);

    JobMatch findByJobMatchId(Integer jobMatchId);
}

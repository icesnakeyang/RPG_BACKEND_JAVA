package com.gogoyang.rpgapi.job.dao;

import com.gogoyang.rpgapi.job.entity.JobMatchLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobMatchLogDao extends JpaRepository<JobMatchLog, Integer>{
    /**
     * 查找某个任务的所有没有处理的匹配记录
     * @param jobId
     * @return
     */
    List<JobMatchLog> findAllByJobIdAndAndProcessResultIsNull(Integer jobId);

    JobMatchLog findByJobIdAndMatchUserIdAndProcessResultIsNull(Integer jobId, Integer userId);
}

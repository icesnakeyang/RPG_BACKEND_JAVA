package com.gogoyang.rpgapi.job.dao;

import com.gogoyang.rpgapi.job.entity.JobApplyLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobApplyLogDao extends JpaRepository<JobApplyLog, Integer>{
    /**
     * 根据userId查询用户没有被处理的任务申请
     * @param userId
     * @return
     */
    List<JobApplyLog> findAllByApplyUserIdAndResultIsNull(Integer userId);

    /**
     * 根据jobId查询所有没有被处理的申请任务的用户
     * @param jobId
     * @return
     */
    List<JobApplyLog> findAllByJobIdAndResultIsNull(Integer jobId);
}

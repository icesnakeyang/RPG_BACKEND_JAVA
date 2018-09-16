package com.gogoyang.rpgapi.job.dao;

import com.gogoyang.rpgapi.job.entity.JobApplyLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobApplyLogDao extends JpaRepository<JobApplyLog, Integer>{
    List<JobApplyLog> findByApplyUserIdAndResultIsNull(Integer userId);
}

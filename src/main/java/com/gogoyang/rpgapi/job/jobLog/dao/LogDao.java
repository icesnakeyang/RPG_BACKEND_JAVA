package com.gogoyang.rpgapi.job.jobLog.dao;

import com.gogoyang.rpgapi.job.jobLog.entity.JobLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogDao extends JpaRepository<JobLog, Integer>{
}

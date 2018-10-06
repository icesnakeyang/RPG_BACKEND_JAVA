package com.gogoyang.rpgapi.job.meta.jobLog.dao;

import com.gogoyang.rpgapi.job.meta.jobLog.entity.JobLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogDao extends JpaRepository<JobLog, Integer>{
}

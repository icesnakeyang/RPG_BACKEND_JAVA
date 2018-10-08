package com.gogoyang.rpgapi.meta.log.dao;

import com.gogoyang.rpgapi.meta.log.entity.JobLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogDao extends JpaRepository<JobLog, Integer>{
}

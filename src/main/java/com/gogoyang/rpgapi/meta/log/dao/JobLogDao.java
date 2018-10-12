package com.gogoyang.rpgapi.meta.log.dao;

import com.gogoyang.rpgapi.meta.log.entity.JobLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobLogDao extends JpaRepository<JobLog, Integer>{
    /**
     * 查找所有userId未读的JobLog
     * @param userId
     * @return
     */
    JobLog findAllByReadTimeIsNullAndCreatedUserIdIsNot(Integer userId);
}

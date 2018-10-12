package com.gogoyang.rpgapi.meta.log.dao;

import com.gogoyang.rpgapi.meta.log.entity.JobLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface JobLogDao extends JpaRepository<JobLog, Integer>{
    /**
     * 查找所有userId未读的JobLog
     * @param userId
     * @return
     */
    ArrayList<JobLog> findAllByReadTimeIsNullAndCreatedUserIdIsNotAndJobId(Integer userId, Integer jobId);

    Page<JobLog> findAllByJobId(Integer jobId, Pageable pageable);
}

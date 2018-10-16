package com.gogoyang.rpgapi.meta.complete.dao;

import com.gogoyang.rpgapi.meta.complete.entity.JobComplete;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface JobCompleteDao extends JpaRepository<JobComplete, Integer>{
    Page<JobComplete> findAllByJobId(Integer jobId, Pageable pageable);

    /**
     * 读取所有我未阅读的验收日志
     * @param userId
     * @param jobId
     * @return
     */
    ArrayList<JobComplete> findAllByReadTimeIsNullAndCreatedUserIdIsNotAndJobId(Integer userId, Integer jobId);
}

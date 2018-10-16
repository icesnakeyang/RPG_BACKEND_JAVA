package com.gogoyang.rpgapi.meta.complete.dao;

import com.gogoyang.rpgapi.meta.complete.entity.JobComplete;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface JobCompleteDao extends JpaRepository<JobComplete, Integer>{
    /**
     * 读取一个任务的所有日志
     */
    Page<JobComplete> findAllByJobId(Integer jobId, Pageable pageable);

    /**
     * 读取所有我未阅读的验收日志
     */
    ArrayList<JobComplete> findAllByReadTimeIsNullAndCreatedUserIdIsNotAndJobId(Integer userId, Integer jobId);

    /**
     * 读取一个任务的所有没有验收处理的申请日志
     */
    ArrayList<JobComplete> findAllByJobIdAndResultIsNull(Integer jobId);
}

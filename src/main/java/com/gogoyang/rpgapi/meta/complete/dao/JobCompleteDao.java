package com.gogoyang.rpgapi.meta.complete.dao;

import com.gogoyang.rpgapi.framework.constant.JobStatus;
import com.gogoyang.rpgapi.framework.constant.LogStatus;
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
     * 读取所有乙方未阅读的验收处理日志
     */
    ArrayList<JobComplete> findAllByProcessReadTimeIsNullAndCreatedUserIdAndJobId(Integer userId, Integer jobId);

    /**
     * 读取一个任务的没有验收处理的申请日志
     */
    JobComplete findByJobIdAndResultIsNull(Integer jobId);

    JobComplete findByJobIdAndResult(Integer jobId, LogStatus logStatus);
}

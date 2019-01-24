package com.gogoyang.rpgapi.meta.complete.dao;

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
     * 读取一个任务的没有验收处理的申请日志
     */
    JobComplete findByJobIdAndResultIsNull(Integer jobId);

    /**
     * 读取指定任务且处于指定状态的验收申请日志
     */
    JobComplete findByJobIdAndResult(Integer jobId, LogStatus logStatus);

    /**
     * 读取甲方未阅读的乙方发起的验收申请
     */
    ArrayList<JobComplete> findAllByReadTimeIsNullAndProcessUserId(Integer partyAId);

    /**
     * 甲方读取一个任务的未阅读的验收日志
     */
    ArrayList<JobComplete> findAllByReadTimeIsNullAndProcessUserIdAndJobId(Integer partyAId, Integer jobId);

    /**
     * 读取乙方未阅读的甲方已验收申请
     */
    ArrayList<JobComplete> findAllByResultAndProcessReadTimeIsNullAndCreatedUserId(LogStatus logStatus, Integer partyBId);

    /**
     * 读取一个任务乙方未阅读的甲方已处理的验收申请
     */
    ArrayList<JobComplete> findAllByResultIsNotNullAndProcessReadTimeIsNullAndCreatedUserIdAndJobId(Integer partyBId, Integer jobId);
}

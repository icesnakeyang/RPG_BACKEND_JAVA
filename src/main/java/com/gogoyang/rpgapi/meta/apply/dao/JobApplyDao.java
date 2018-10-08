package com.gogoyang.rpgapi.meta.apply.dao;

import com.gogoyang.rpgapi.meta.apply.entity.JobApply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface JobApplyDao extends JpaRepository<JobApply, Integer> {
    /**
     * 根据jobId查询所有没有被处理的申请任务的用户
     * @param jobId
     * @return
     */
    ArrayList<JobApply> findAllByJobIdAndProcessResultIsNull(Integer jobId);

    /**
     * 查找userId用户申请的jobId任务的申请日志
     * @param applyUserId
     * @param jobId
     * @return
     */
    JobApply findByApplyUserIdAndJobIdAndProcessResultIsNull(Integer applyUserId, Integer jobId);

    /**
     * 查找所有未成交的任务申请日志
     * @param pageable
     * @return
     */
    Page<JobApply> findAllByProcessResultIsNull(Pageable pageable);

    /**
     * 查找所有userId申请但还没处理的申请日志
     * @param userId
     * @return
     */
    ArrayList<JobApply> findAllByApplyUserIdAndProcessResultIsNull(Integer userId);

}

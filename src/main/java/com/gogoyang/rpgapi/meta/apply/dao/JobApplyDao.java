package com.gogoyang.rpgapi.meta.apply.dao;

import com.gogoyang.rpgapi.meta.apply.entity.JobApply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.reflect.Array;
import java.util.ArrayList;

public interface JobApplyDao extends JpaRepository<JobApply, Integer> {
    /**
     * 查找一个任务的所有申请
     * @param jobId
     * @return
     */
    ArrayList<JobApply> findAllByJobIdAndProcessResultIsNull(Integer jobId);

    /**
     * 查找一个用户申请的一个任务的申请记录
     * @param applyUserId
     * @param jobId
     * @return
     */
    JobApply findByApplyUserIdAndJobIdAndProcessResultIsNull(Integer applyUserId, Integer jobId);

    /**
     * 查找所有未处理的任务申请
     * @param pageable
     * @return
     */
    Page<JobApply> findAllByProcessResultIsNull(Pageable pageable);

    /**
     * 查找一个用户的所有未处理的任务申请
     * @param userId
     * @return
     */
    ArrayList<JobApply> findAllByApplyUserIdAndProcessResultIsNull(Integer userId);

    Page<JobApply> findAllByApplyUserId(Integer userId, Pageable pageable);

}

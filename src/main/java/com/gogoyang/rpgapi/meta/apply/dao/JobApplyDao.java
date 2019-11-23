package com.gogoyang.rpgapi.meta.apply.dao;

import com.gogoyang.rpgapi.framework.constant.LogStatus;
import com.gogoyang.rpgapi.meta.apply.entity.JobApply;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.reflect.Array;
import java.util.ArrayList;

public interface JobApplyDao extends JpaRepository<JobApply, Integer> {
    JobApply findByJobApplyId(Integer applyId);

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

    /**
     * 查询一个用户的所有任务申请
     * @param userId
     * @param pageable
     * @return
     */
    Page<JobApply> findAllByApplyUserId(Integer userId, Pageable pageable);

    /**
     * 查询一个用户的所有已处理未阅读，指定处理结果状态
     * @param userId
     * @param logStatus
     * @return
     */
    ArrayList<JobApply> findAllByProcessResultReadTimeIsNullAndApplyUserIdAndProcessResult(Integer userId, LogStatus logStatus);

}

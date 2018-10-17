package com.gogoyang.rpgapi.meta.stop.dao;

import com.gogoyang.rpgapi.meta.stop.entity.JobStop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface JobStopDao extends JpaRepository<JobStop, Integer>{
    /**
     * 获取一个任务的所有终止申请
     */
    Page<JobStop> findAllByJobId(Integer jobId, Pageable pageable);

    /**
     * 获取一个任务一个用户未读的终止申请
     */
    ArrayList<JobStop> findAllByJobIdAndReadTimeIsNullAndCreatedUserIdIsNot(Integer jobId, Integer userId);

    /**
     * 获取一个未处理的终止申请
     */
    JobStop findByJobIdAndResultIsNull(Integer jobId);
}

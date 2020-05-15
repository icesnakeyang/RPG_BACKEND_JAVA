package com.gogoyang.rpgapi.meta.stop.dao;

import com.gogoyang.rpgapi.meta.stop.entity.JobStop;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.Map;

@Mapper
public interface JobStopDao{
    /**
     * 创建一个任务终止日志
     * @param jobStop
     */
    void createJobStop(JobStop jobStop);

    /**
     * 读取任务终止日志
     * @param stopId
     * @return
     */
    JobStop getJobStop(String stopId);

    /**
     * 批量查询任务终止日志
     * @param qIn
     * jobId
     * createdUserId
     * processUserId
     * unread
     * userId
     * unreadProcess
     * offset
     * size
     * @return
     */
    ArrayList<JobStop> listJobStop(Map qIn);

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

    void updateJobStop(JobStop jobStop);
}

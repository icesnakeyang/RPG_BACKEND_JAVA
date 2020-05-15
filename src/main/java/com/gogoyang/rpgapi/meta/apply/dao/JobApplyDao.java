package com.gogoyang.rpgapi.meta.apply.dao;

import com.gogoyang.rpgapi.meta.apply.entity.JobApply;
import org.apache.ibatis.annotations.Mapper;

import java.util.ArrayList;
import java.util.Map;

@Mapper
public interface JobApplyDao {
    void createJobApply(JobApply jobApply);

    /**
     * 读取一个任务申请信息
     * @param qIn
     * jobApplyId
     * @return
     */
    JobApply getJobApply(Map qIn);

    /**
     * 批量查询任务申请
     * @param qIn
     * jobApplyId
     * jobId
     * status
     * processUserId
     * applyUserId
     * offset
     * size
     * @return
     */
    ArrayList<JobApply> listJobApply(Map qIn);

    /**
     * 统计任务申请信息
     * @param qIn
     * jobId
     * status
     * @return
     */
    Integer totalJobApply(Map qIn);

    void updateJobApply(JobApply jobApply);

}

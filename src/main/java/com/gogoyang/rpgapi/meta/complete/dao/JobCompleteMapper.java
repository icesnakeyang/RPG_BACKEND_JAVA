package com.gogoyang.rpgapi.meta.complete.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface JobCompleteMapper {
    /**
     * 统计所有未阅读的任务完成日志
     *
     * @param qIn
     * @return
     */
    Integer totalUnreadComplete(Map qIn);

    /**
     * 把所有未阅读的验收日志设置为当前阅读时间
     * @param qIn
     */
    void setJobCompleteReadTime(Map qIn);
}

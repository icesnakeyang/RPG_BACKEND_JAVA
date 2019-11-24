package com.gogoyang.rpgapi.meta.log.dao;

import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface JobLogMapper {
    /**
     * 统计所有未阅读的任务日志
     *
     * @param qIn
     * @return
     */
    Integer totalUnreadLog(Map qIn);

    /**
     * 设置当前用户所有未读的任务日志的阅读时间为当前时间
     * @param qIn
     */
    void setJobLogReadTime(Map qIn);
}

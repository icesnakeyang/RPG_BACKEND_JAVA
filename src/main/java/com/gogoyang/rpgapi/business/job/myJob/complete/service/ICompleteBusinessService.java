package com.gogoyang.rpgapi.business.job.myJob.complete.service;

import com.gogoyang.rpgapi.meta.complete.entity.JobComplete;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface ICompleteBusinessService {

    void createJobComplete(Map in) throws Exception;

    Page<JobComplete> loadCompleteList(Map in) throws Exception;

    void setCompleteReadTime(Map in) throws Exception;

    /**
     * 拒绝验收
     * @param in
     * @throws Exception
     */
    void rejectComplete(Map in) throws Exception;

    void acceptComplete(Map in) throws Exception;

    /**
     * 统计未阅读的验收日志数量
     * @param in
     * @return
     * @throws Exception
     */
    Integer countUnreadComplete(Map in) throws Exception;
}
package com.gogoyang.rpgapi.business.job.stop.service;

import com.gogoyang.rpgapi.meta.stop.entity.JobStop;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface IStopBusinessService {

    void createJobStop(Map in) throws Exception;

    Page<JobStop> loadStopList(Map in) throws Exception;

    void setStopReadTime(Map in) throws Exception;

    /**
     * 拒绝终止
     * @param in
     * @throws Exception
     */
    void rejectStop(Map in) throws Exception;

    /**
     * 同意终止
     * @param in
     * @throws Exception
     */
    void acceptStop(Map in) throws Exception;

    /**
     * 统计未阅读的终止日志数量
     * @param in
     * @return
     * @throws Exception
     */
    Integer countUnreadStop(Map in) throws Exception;
}
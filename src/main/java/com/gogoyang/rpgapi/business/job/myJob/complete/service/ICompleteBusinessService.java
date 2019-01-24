package com.gogoyang.rpgapi.business.job.myJob.complete.service;

import com.gogoyang.rpgapi.meta.complete.entity.JobComplete;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface ICompleteBusinessService {

    void createJobComplete(Map in) throws Exception;

    Page<JobComplete> listMyComplete(Map in) throws Exception;

    void setCompleteReadTime(Map in) throws Exception;

    /**
     * 拒绝验收
     * @param in
     * @throws Exception
     */
    void rejectComplete(Map in) throws Exception;

    void acceptComplete(Map in) throws Exception;

    Map listMyPartyAAcceptJob(Map in) throws Exception;

    Map listMyPartyBAcceptJob(Map in) throws Exception;
}

package com.gogoyang.rpgapi.business.job.complete.service;

import com.gogoyang.rpgapi.meta.complete.entity.JobComplete;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface ICompleteBusinessService {
    void createJobComplete(Map in) throws Exception;
    Page<JobComplete> loadCompleteList(Map in) throws Exception;

    void setCompleteReadTime(Map in) throws Exception;
}

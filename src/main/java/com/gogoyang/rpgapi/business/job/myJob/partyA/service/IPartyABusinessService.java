package com.gogoyang.rpgapi.business.job.myJob.partyA.service;

import com.gogoyang.rpgapi.meta.job.entity.Job;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface IPartyABusinessService {
    Page<Job> listMyPartyAJob(Map in) throws Exception;

    Map getPartyAJob(Map in) throws Exception;

    Map totalUnreadByJobId(Map in) throws Exception;
}

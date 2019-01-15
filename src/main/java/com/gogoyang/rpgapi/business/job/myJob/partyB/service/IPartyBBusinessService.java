package com.gogoyang.rpgapi.business.job.myJob.partyB.service;

import com.gogoyang.rpgapi.meta.job.entity.Job;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface IPartyBBusinessService {
    Page<Job> listMyPartyBJob(Map in) throws Exception;

    Map getPartyBJobDetail(Map in) throws Exception;
}

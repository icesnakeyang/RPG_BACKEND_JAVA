package com.gogoyang.rpgapi.business.job.myJob.partyB.service;

import com.gogoyang.rpgapi.meta.job.entity.Job;

import java.util.ArrayList;
import java.util.Map;

public interface IPartyBBusinessService {
    ArrayList<Job> listMyPartyBJob(Map in) throws Exception;

    Map getPartyBJobDetail(Map in) throws Exception;
}

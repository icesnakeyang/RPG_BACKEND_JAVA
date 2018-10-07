package com.gogoyang.rpgapi.job.business.publish;

import com.gogoyang.rpgapi.job.meta.job.entity.Job;

import java.util.Map;

public interface IPublishJobService {
    Job publishJob(Map in) throws Exception;
}

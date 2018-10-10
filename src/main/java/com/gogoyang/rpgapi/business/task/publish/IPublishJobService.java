package com.gogoyang.rpgapi.business.task.publish;

import com.gogoyang.rpgapi.meta.job.entity.Job;

import java.util.Map;

public interface IPublishJobService {
    Job publishJob(Map in) throws Exception;
}

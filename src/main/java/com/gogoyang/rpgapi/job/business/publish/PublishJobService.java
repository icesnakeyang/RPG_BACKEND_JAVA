package com.gogoyang.rpgapi.job.business.publish;

import com.gogoyang.rpgapi.job.meta.job.entity.Job;
import com.gogoyang.rpgapi.job.meta.job.service.IJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Map;

@Service
public class PublishJobService implements IPublishJobService{
    private final IJobService iJobService;

    @Autowired
    public PublishJobService(IJobService iJobService) {
        this.iJobService = iJobService;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void publishJob(Map in) throws Exception {
        Job job=new Job();
        Integer userId=(Integer)in.get("userId");
        job.setCreatedUserId(userId);
        job.setCreatedTime(new Date());
        job.set

    }
}

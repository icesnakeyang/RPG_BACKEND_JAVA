package com.gogoyang.rpgapi.job.business.publish;

import com.gogoyang.rpgapi.constant.JobStatus;
import com.gogoyang.rpgapi.job.meta.job.entity.Job;
import com.gogoyang.rpgapi.job.meta.job.entity.JobDetail;
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
    public Job publishJob(Map in) throws Exception {
        Job job=new Job();
        job.setCreatedUserId((Integer)in.get("userId"));
        job.setCreatedTime(new Date());
        job.setCode(in.get("code").toString());
        job.setDays((Integer)in.get("days"));
        job.setReward((Double)in.get("price"));
        job.setStatus(JobStatus.MATCHING);
        job.setTaskId((Integer)in.get("taskId"));
        job.setTitle(in.get("title").toString());
        job.setDetail(in.get("detail").toString());

        job=iJobService.insertJob(job);

        return job;
    }
}

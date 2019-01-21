package com.gogoyang.rpgapi.business.job.myJob.common.service;

import com.gogoyang.rpgapi.business.job.myJob.complete.service.ICompleteBusinessService;
import com.gogoyang.rpgapi.business.job.myJob.log.service.IMyLogBusinessService;
import com.gogoyang.rpgapi.business.job.myJob.stop.service.IStopBusinessService;
import com.gogoyang.rpgapi.meta.job.entity.Job;
import com.gogoyang.rpgapi.meta.job.service.IJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MyJobCommonBusinessService implements IMyJobCommonBusinessService {
    private final IMyLogBusinessService iMyLogBusinessService;
    private final ICompleteBusinessService iCompleteBusinessService;
    private final IStopBusinessService iStopBusinessService;
    private final IJobService iJobService;

    @Autowired
    public MyJobCommonBusinessService(IMyLogBusinessService iMyLogBusinessService,
                                      ICompleteBusinessService iCompleteBusinessService,
                                      IStopBusinessService iStopBusinessService,
                                      IJobService iJobService) {
        this.iMyLogBusinessService = iMyLogBusinessService;
        this.iCompleteBusinessService = iCompleteBusinessService;
        this.iStopBusinessService = iStopBusinessService;
        this.iJobService = iJobService;
    }

    /**
     * 读取任务的各种状态，未读事件
     *
     * @param in
     * @return
     * @throws Exception
     */
    @Override
    public Map loadUnreadByJobId(Map in) throws Exception {
        String token = in.get("token").toString();
        Integer jobId = (Integer) in.get("jobId");
        Map out = new HashMap();
        out.put("unReadJobLog", iMyLogBusinessService.countUnreadJobLog(in));
        out.put("unReadComplete", iCompleteBusinessService.countUnreadComplete(in));
        out.put("unReadStop", iStopBusinessService.countUnreadStop(in));
        return out;
    }

    /**
     * 根据taskId获取job
     *
     * @param in
     * @return
     * @throws Exception
     */
    @Override
    public Map getJobTinyByTaskId(Map in) throws Exception {
        Integer taskId = (Integer) in.get("taskId");
        Job job = iJobService.getJobByTaskId(taskId);
        Map out = new HashMap();
        out.put("job", job);
        return out;
    }

    @Override
    public Map getJobTinyByJobId(Map in) throws Exception {
        Integer jobId = (Integer) in.get("jobId");
        Job job = iJobService.getJobByJobIdTiny(jobId);
        Map out = new HashMap();
        out.put("job", job);
        return out;
    }
}

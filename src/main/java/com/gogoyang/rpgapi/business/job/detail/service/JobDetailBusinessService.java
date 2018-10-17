package com.gogoyang.rpgapi.business.job.detail.service;

import com.gogoyang.rpgapi.business.job.complete.service.ICompleteBusinessService;
import com.gogoyang.rpgapi.business.job.jobLog.service.IJobLogBusinessService;
import com.gogoyang.rpgapi.meta.job.entity.Job;
import com.gogoyang.rpgapi.meta.job.service.IJobService;
import com.gogoyang.rpgapi.meta.user.userInfo.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class JobDetailBusinessService implements IJobDetailBusinessService{
    private final IJobService iJobService;
    private final IJobLogBusinessService iJobLogBusinessService;
    private final IUserInfoService iUserInfoService;
    private final ICompleteBusinessService iCompleteBusinessService;

    @Autowired
    public JobDetailBusinessService(IJobService iJobService, IJobLogBusinessService iJobLogBusinessService, IUserInfoService iUserInfoService, ICompleteBusinessService iCompleteBusinessService) {
        this.iJobService = iJobService;
        this.iJobLogBusinessService = iJobLogBusinessService;
        this.iUserInfoService = iUserInfoService;
        this.iCompleteBusinessService = iCompleteBusinessService;
    }

    /**
     * 读取任务详情
     * @param in
     * @return
     * @throws Exception
     */
    @Override
    public Map loadJobDetail(Map in) throws Exception {
        Integer jobId=(Integer)in.get("jobId");
        Job job = iJobService.loadJobByJobId(jobId);
        Map out=new HashMap();
        out.put("job", job);
        return out;
    }

    /**
     * 读取一条任务的简要信息
     * @param in
     * @return
     * @throws Exception
     */
    @Override
    public Map loadJobTiny(Map in) throws Exception {
        Integer jobId=(Integer)in.get("jobId");
        Job job=iJobService.loadJobByJobIdTiny(jobId);
        Map out=new HashMap();
        out.put("job", job);
        return out;
    }

    /**
     * 读取任务的各种状态，未读事件
     * @param in
     * @return
     * @throws Exception
     */
    @Override
    public Map loadUnreadByJobId(Map in) throws Exception {
        String token=in.get("token").toString();
        Integer jobId=(Integer)in.get("jobId");
        Map out=new HashMap();
        out.put("unReadJobLog",iJobLogBusinessService.countUnreadJobLog(in));
        out.put("unReadComplete", iCompleteBusinessService.countUnreadComplete(in));
        return out;
    }
}

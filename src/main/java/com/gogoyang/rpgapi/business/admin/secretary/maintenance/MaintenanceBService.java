package com.gogoyang.rpgapi.business.admin.secretary.maintenance;

import com.gogoyang.rpgapi.framework.common.GogoTools;
import com.gogoyang.rpgapi.framework.constant.JobStatus;
import com.gogoyang.rpgapi.meta.admin.entity.Admin;
import com.gogoyang.rpgapi.meta.admin.service.IAdminService;
import com.gogoyang.rpgapi.meta.job.entity.Job;
import com.gogoyang.rpgapi.meta.job.service.IJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class MaintenanceBService implements IMaintenanceBService {
    private final IAdminService iAdminService;
    private final IJobService iJobService;

    public MaintenanceBService(IAdminService iAdminService,
                               IJobService iJobService) {
        this.iAdminService = iAdminService;
        this.iJobService = iJobService;
    }

    /**
     * 检查未成交任务时间，把超过期限的任务设置为过期
     *
     * @param in
     * @throws Exception
     */
    @Override
    public void overdueJobs(Map in) throws Exception {
        String token = in.get("token").toString();

        Admin admin = iAdminService.getAdminByToken(token);
        if (admin == null) {
            throw new Exception("10004");
        }

        Map jobMap = iJobService.listPublicJob(null, null);
        ArrayList<Job> jobs = (ArrayList<Job>) jobMap.get("jobs");
        for (int i = 0; i < jobs.size(); i++) {
            Job job = jobs.get(i);
            if (job.getStatus().equals("PENDING")) {
                // 判断任务是否过期
                Date createTime = job.getCreatedTime();
                Date now = new Date();
                Long timeOver = GogoTools.dateDiff(createTime, now);
                Integer overdueDays = 30;
                Integer timeOver2 = overdueDays * 24 * 60 * 60 * 1000;
                if (timeOver > timeOver2) {
                    //任务已过期，设置为overdue状态
                    Job job2 = new Job();
                    job2.setJobId(job.getJobId());
                    job2.setStatus(JobStatus.OVERDUE.toString());
                    iJobService.updateJob(job2);
                }
            }
        }
    }
}

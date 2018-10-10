package com.gogoyang.rpgapi.business.job.myMatch.service;

import com.gogoyang.rpgapi.meta.apply.service.IJobApplyService;
import com.gogoyang.rpgapi.meta.job.entity.Job;
import com.gogoyang.rpgapi.meta.job.service.IJobService;
import com.gogoyang.rpgapi.meta.match.entity.JobMatch;
import com.gogoyang.rpgapi.meta.match.service.IJobMatchService;
import com.gogoyang.rpgapi.meta.user.userInfo.entity.UserInfo;
import com.gogoyang.rpgapi.meta.user.userInfo.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class MyMatchBusinessService implements IMyMatchBusinessService {
    private final IUserInfoService iUserInfoService;
    private final IJobMatchService iJobMatchService;
    private final IJobService iJobService;
    private final IJobApplyService iJobApplyService;

    @Autowired
    public MyMatchBusinessService(IUserInfoService iUserInfoService, IJobMatchService iJobMatchService, IJobService iJobService, IJobApplyService iJobApplyService) {
        this.iUserInfoService = iUserInfoService;
        this.iJobMatchService = iJobMatchService;
        this.iJobService = iJobService;
        this.iJobApplyService = iJobApplyService;
    }

    /**
     * 读取所有RPG秘书分配给我的新任务
     * @param in
     * @return
     * @throws Exception
     */
    @Override
    public Map loadJobMatchToMe(Map in) throws Exception {
        /**
         * 参数：token
         * 1、根据token读取userInfo
         * 2、根据userId读取所有jobMatch
         * 3、逐条根据jobMatch读取所有job
         */
        String token = in.get("token").toString();
        UserInfo userInfo = iUserInfoService.checkToken(token);
        if (userInfo == null) {
            throw new Exception("10004");
        }

        ArrayList<JobMatch> newMatchs = iJobMatchService.loadMyNewJobMatch(userInfo.getUserId());
        ArrayList newJobs = new ArrayList();
        for (int i = 0; i < newMatchs.size(); i++) {
            Map map = new HashMap();
            Job job = iJobService.loadJobByJobIdTiny(newMatchs.get(i).getJobId());
            if (job != null) {
                map.put("match", newMatchs.get(i));
                job.setPartyAName(iUserInfoService.getUserName(job.getPartyAId()));
                job.setJobApplyNum(iJobApplyService.countApplyUsers(job.getJobId()));
                job.setJobMatchNum(iJobMatchService.countMatchingUsers(job.getJobId()));
                map.put("job", job);
                newJobs.add(map);
            }
        }
        Map out = new HashMap();
        out.put("newJobs", newJobs);
        return out;
    }
}

package com.gogoyang.rpgapi.business.job.myJob.spotlight.service;

import com.gogoyang.rpgapi.framework.common.GogoTools;
import com.gogoyang.rpgapi.framework.common.ICommonBusinessService;
import com.gogoyang.rpgapi.framework.constant.GogoStatus;
import com.gogoyang.rpgapi.meta.job.entity.Job;
import com.gogoyang.rpgapi.meta.job.service.IJobService;
import com.gogoyang.rpgapi.meta.spotlight.entity.Spotlight;
import com.gogoyang.rpgapi.meta.spotlight.service.ISpotlightService;
import com.gogoyang.rpgapi.meta.user.entity.UserInfo;
import com.gogoyang.rpgapi.meta.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class MySpotBusinessService implements IMySpotBusinessService {
    private final ISpotlightService iSpotlightService;
    private final IJobService iJobService;
    private final IUserService iUserService;
    private final ICommonBusinessService iCommonBusinessService;

    @Autowired
    public MySpotBusinessService(ISpotlightService iSpotlightService,
                                 IJobService iJobService,
                                 IUserService iUserService,
                                 ICommonBusinessService iCommonBusinessService) {
        this.iSpotlightService = iSpotlightService;
        this.iJobService = iJobService;
        this.iUserService = iUserService;
        this.iCommonBusinessService = iCommonBusinessService;
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public void createSpotlight(Map in) throws Exception {
        /**
         * 1、根据jobId读取任务信息。无论任务处于什么状态，其实都可以申诉
         * 2、根据token获取当前用户。用户必须是甲方，或者乙方才能创建申诉
         * 3、如果任务当前已经有未撤销的申诉，则无论甲方或者乙方都不能再次发起申诉
         * 4、创建申诉
         * 5、创建申诉后，甲方和乙方都要扣除任务金额price对应的荣誉值honor
         * 6、更新userinfo里的honor
         */
        String token = in.get("token").toString();
        String jobId = in.get("jobId").toString();
        String title = in.get("title").toString();
        String content = in.get("content").toString();

        //读取任务
        Job job = iCommonBusinessService.getJobTinyByJobId(jobId);

        //如果任务当前已经有未撤销的申诉，则无论甲方或者乙方都不能再次发起申诉
        Map qIn = new HashMap();
        qIn.put("jobId", jobId);
        qIn.put("status", GogoStatus.ACTIVE);
        ArrayList<Spotlight> spotlights = iSpotlightService.listSpotlight(qIn);
        if (spotlights.size() > 0) {
            //当前任务存在正在申诉事件，不能发起新的申诉
            throw new Exception("30002");
        }

        //读取当前用户
        UserInfo user = iCommonBusinessService.getUserByToken(token);

        //当前用户必须是甲方或者乙方
        if (!user.getUserId().equals(job.getPartyAId()) &&
                !user.getUserId().equals(job.getPartyBId())) {
            throw new Exception("10090");
        }

        //创建申诉
        Spotlight spotlight = new Spotlight();
        spotlight.setSpotlightId(GogoTools.UUID());
        spotlight.setJobId(jobId);
        spotlight.setCreatedUserId(user.getUserId());
        spotlight.setCreatedTime(new Date());
        spotlight.setTitle(title);
        spotlight.setContent(content);
        spotlight.setStatus(GogoStatus.ACTIVE.toString());
        iSpotlightService.insertSpotlight(spotlight);

        //创建申诉后，甲方和乙方都要扣除任务金额price对应的荣誉值honor
        UserInfo userA = iUserService.getUserByUserId(job.getPartyAId());
        double honor = 0.0;
        if (userA.getHonor() != null) {
            honor = userA.getHonor();
        }
        honor -= job.getPrice();
        userA.setHonor(honor);

        honor = 0.0;
        if (userA.getHonorOut() != null) {
            honor = userA.getHonorOut();
        }
        honor += job.getPrice();
        userA.setHonorOut(honor);

        UserInfo userB = iUserService.getUserByUserId(job.getPartyBId());
        honor = 0.0;
        if (userB.getHonor() != null) {
            honor = userB.getHonor();
        }
        honor -= job.getPrice();
        userB.setHonor(honor);

        honor = 0.0;
        if (userB.getHonorOut() != null) {
            honor = userB.getHonorOut();
        }
        honor += job.getPrice();
        userB.setHonorOut(honor);

        iUserService.updateUserInfo(userA);
        iUserService.updateUserInfo(userB);
    }

    @Override
    public Map listMySpotlight(Map in) throws Exception {
        String token = in.get("token").toString();
        Integer pageIndex = (Integer) in.get("pageIndex");
        Integer pageSize = (Integer) in.get("pageSize");

        UserInfo user = iCommonBusinessService.getUserByToken(token);
        Map qIn = new HashMap();
        qIn.put("userId", user.getUserId());
        Integer offset = (pageIndex - 1) * pageSize;
        qIn.put("offset", offset);
        qIn.put("size", pageSize);
        ArrayList<Spotlight> spotlights = iSpotlightService.listSpotlight(qIn);

        Map out = new HashMap();
        out.put("spotlights", spotlights);
        return out;
    }
}

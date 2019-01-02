package com.gogoyang.rpgapi.business.job.myJob.mySpotlight.service;

import com.gogoyang.rpgapi.meta.job.entity.Job;
import com.gogoyang.rpgapi.meta.job.service.IJobService;
import com.gogoyang.rpgapi.meta.spotlight.entity.Spot;
import com.gogoyang.rpgapi.meta.spotlight.service.ISpotService;
import com.gogoyang.rpgapi.meta.user.entity.User;
import com.gogoyang.rpgapi.meta.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class MySpotBusinessService implements IMySpotBusinessService {
    private final ISpotService iSpotService;
    private final IJobService iJobService;
    private final IUserService iUserService;

    @Autowired
    public MySpotBusinessService(ISpotService iSpotService,
                                 IJobService iJobService,
                                 IUserService iUserService) {
        this.iSpotService = iSpotService;
        this.iJobService = iJobService;
        this.iUserService = iUserService;
    }

    @Override
    public Map createSpotlight(Map in) throws Exception {
        /**
         * 1、根据jobId读取任务信息。无论任务处于什么状态，其实都可以申诉
         * 2、根据token获取当前用户。用户必须是甲方，或者乙方才能创建申诉
         * 3、如果任务当前已经有未撤销的申诉，则无论甲方或者乙方都不能再次发起申诉
         * 4、创建申诉
         * 5、创建申诉后，甲方和乙方都要扣除任务金额price对应的荣誉值honor
         * 6、更新userinfo里的honor
         */
        String token = in.get("token").toString();
        Integer jobId = (Integer) in.get("jobId");
        String title = in.get("title").toString();
        String content = in.get("content").toString();

        Job job = iJobService.getJobByJobIdTiny(jobId);

        User user = iUserService.getUserByToken(token);
        if (user.getUserId() != job.getPartyAId()) {
            if (user.getUserId() != job.getPartyBId()) {
                throw new Exception("10090");
            }
        }

        Spot spot = new Spot();

        spot.setTitle(title);
        spot.setJobId(jobId);
        spot.setCreatedUserId(user.getUserId());
        spot.setCreatedTime(new Date());
        spot.setContent(content);
        spot = iSpotService.insertSpotlight(spot);

        User userA = iUserService.getUserByUserId(job.getPartyAId());
        Double honor = 0.0;
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

        User userB = iUserService.getUserByUserId(job.getPartyBId());
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

        iUserService.update(userA);
        iUserService.update(userB);

        Map out = new HashMap();
        out.put("spot", spot);
        return out;
    }

    @Override
    public Page<Spot> listMySpotlight(Map in) throws Exception {
        Integer jobId = (Integer) in.get("jobId");
        String token = in.get("token").toString();
        Integer pageIndex=(Integer)in.get("pageIndex");
        Integer pageSize=(Integer)in.get("pageSize");
        User user = iUserService.getUserByToken(token);
        Job job = iJobService.getJobByJobIdTiny(jobId);
        if (user.getUserId() != job.getPartyAId()) {
            if (user.getUserId() != job.getPartyBId()) {
                throw new Exception("10089");
            }
        }
        Page<Spot> spots = iSpotService.listSpotlightByJobId(jobId, pageIndex, pageSize);
        return spots;
    }
}

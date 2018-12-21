package com.gogoyang.rpgapi.business.job.mySpotlight.service;

import ch.qos.logback.core.encoder.EchoEncoder;
import com.gogoyang.rpgapi.meta.job.entity.Job;
import com.gogoyang.rpgapi.meta.job.service.IJobService;
import com.gogoyang.rpgapi.meta.spotlight.entity.Spot;
import com.gogoyang.rpgapi.meta.spotlight.service.ISpotService;
import com.gogoyang.rpgapi.meta.user.userInfo.entity.UserInfo;
import com.gogoyang.rpgapi.meta.user.userInfo.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class MySpotBusinessService implements IMySpotBusinessService {
    private final ISpotService iSpotService;
    private final IUserInfoService iUserInfoService;
    private final IJobService iJobService;

    @Autowired
    public MySpotBusinessService(ISpotService iSpotService, IUserInfoService iUserInfoService, IJobService iJobService) {
        this.iSpotService = iSpotService;
        this.iUserInfoService = iUserInfoService;
        this.iJobService = iJobService;
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

        UserInfo userInfo = iUserInfoService.loadUserByToken(token);
        if (userInfo.getUserId() != job.getPartyAId()) {
            if (userInfo.getUserId() != job.getPartyBId()) {
                throw new Exception("10090");
            }
        }

        Spot spot = iSpotService.getSpotlightByJobId(jobId);
        if (spot != null) {
            throw new Exception("10091");
        }

        spot = new Spot();
        spot.setTitle(title);
        spot.setJobId(jobId);
        spot.setCreatedUserId(userInfo.getUserId());
        spot.setCreatedTime(new Date());
        spot.setContent(content);
        spot = iSpotService.insertSpotlight(spot);

        UserInfo userA = iUserInfoService.loadUserByUserId(job.getPartyAId());
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

        UserInfo userB = iUserInfoService.loadUserByUserId(job.getPartyBId());
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

        iUserInfoService.updateUser(userA);
        iUserInfoService.updateUser(userB);

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
        UserInfo userInfo = iUserInfoService.loadUserByToken(token);
        Job job = iJobService.getJobByJobIdTiny(jobId);
        if (userInfo.getUserId() != job.getPartyAId()) {
            if (userInfo.getUserId() != job.getPartyBId()) {
                throw new Exception("10089");
            }
        }
        Page<Spot> spots = iSpotService.listSpotlightByJobId(jobId, pageIndex, pageSize);
        return spots;
    }
}

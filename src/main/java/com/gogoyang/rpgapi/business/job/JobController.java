package com.gogoyang.rpgapi.business.job;

import com.gogoyang.rpgapi.framework.constant.JobStatus;
import com.gogoyang.rpgapi.meta.job.entity.Job;
import com.gogoyang.rpgapi.meta.apply.entity.JobApply;
import com.gogoyang.rpgapi.meta.apply.service.IJobApplyService;
import com.gogoyang.rpgapi.meta.match.entity.JobMatch;
import com.gogoyang.rpgapi.meta.match.service.IJobMatchService;
import com.gogoyang.rpgapi.meta.job.service.IJobService;
import com.gogoyang.rpgapi.meta.user.userInfo.entity.UserInfo;
import com.gogoyang.rpgapi.meta.user.userInfo.service.IUserInfoService;
import com.gogoyang.rpgapi.business.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/job")
public class JobController {
    private final IJobService iJobService;
    private final IUserInfoService iUserInfoService;
    private final IJobApplyService iJobApplyService;
    private final IJobMatchService iJobMatchService;

    @Autowired
    public JobController(IJobService iJobService, IUserInfoService iUserInfoService, IJobApplyService iJobApplyService,
                         IJobMatchService iJobMatchService) {
        this.iJobService = iJobService;
        this.iUserInfoService = iUserInfoService;
        this.iJobApplyService = iJobApplyService;
        this.iJobMatchService = iJobMatchService;
    }







}

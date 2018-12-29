package com.gogoyang.rpgapi.business.job;

import com.gogoyang.rpgapi.meta.apply.service.IJobApplyService;
import com.gogoyang.rpgapi.meta.match.service.IJobMatchService;
import com.gogoyang.rpgapi.meta.job.service.IJobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/job")
public class JobController {
    private final IJobService iJobService;
    private final IJobApplyService iJobApplyService;
    private final IJobMatchService iJobMatchService;

    @Autowired
    public JobController(IJobService iJobService,
                         IJobApplyService iJobApplyService,
                         IJobMatchService iJobMatchService) {
        this.iJobService = iJobService;
        this.iJobApplyService = iJobApplyService;
        this.iJobMatchService = iJobMatchService;
    }







}

package com.gogoyang.rpgapi.job.controller;

import com.gogoyang.rpgapi.job.service.IJobService;
import com.gogoyang.rpgapi.job.vo.CreateJobRequest;
import com.gogoyang.rpgapi.user.entity.User;
import com.gogoyang.rpgapi.user.service.IUserService;
import com.gogoyang.rpgapi.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/job")
public class JobController {
    private final IJobService jobService;
    private final IUserService userService;

    @Autowired
    public JobController(IJobService jobService, IUserService userService) {
        this.jobService = jobService;
        this.userService = userService;
    }

    @ResponseBody
    @PostMapping("/publish")
    public Response CreateJob(@RequestBody CreateJobRequest request,
                              HttpServletRequest servletRequest){
        String token=servletRequest.getHeader("token");
        User user=userService.buildUserByToken(token);
        request.setCreatedUserId(user.getUserId());
        return jobService.createJob(request);
    }

    @ResponseBody
    @GetMapping("/jobPlaza/{category}")
    public Response JobPlaza(@PathVariable String category){
        return jobService.buildJobs(category);
    }
}

package com.gogoyang.rpgapi.job.controller;

import com.gogoyang.rpgapi.job.service.IJobService;
import com.gogoyang.rpgapi.job.vo.ApplyJobRequest;
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

    @ResponseBody
    @GetMapping("/{jobId}")
    public Response JobDetail(@PathVariable Integer jobId){
        return jobService.buildJobInfo(jobId);
    }

    @ResponseBody
    @PostMapping("/applyJob")
    public Response JobApply(@RequestBody ApplyJobRequest applyJobRequest,
                             HttpServletRequest httpServletRequest){
        Response response=new Response();
        String token=httpServletRequest.getHeader("token");
        if(token==null){
            response.setErrorCode(10001);
            return response;
        }
        if(applyJobRequest.getJobId()==null){
            response.setErrorCode(10002);
            return response;
        }
        applyJobRequest.setToken(token);
        try{
            jobService.applyJob(applyJobRequest);
        }catch (Exception ex){
            response.setErrorCode(10002);
            response.setErrorMsg(ex.getMessage());
            return response;
        }
        return response;
    }
}

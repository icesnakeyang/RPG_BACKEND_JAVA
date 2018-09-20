package com.gogoyang.rpgapi.job.controller;

import com.gogoyang.rpgapi.common.IRPGFUNC;
import com.gogoyang.rpgapi.job.entity.Job;
import com.gogoyang.rpgapi.job.service.IJobApplyLogService;
import com.gogoyang.rpgapi.job.service.IJobMatchLogService;
import com.gogoyang.rpgapi.job.service.IJobService;
import com.gogoyang.rpgapi.job.vo.ApplyJobRequest;
import com.gogoyang.rpgapi.job.vo.CreateJobRequest;
import com.gogoyang.rpgapi.user.entity.User;
import com.gogoyang.rpgapi.user.service.IUserService;
import com.gogoyang.rpgapi.vo.Request;
import com.gogoyang.rpgapi.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/job")
public class JobController {
    private final IJobService jobService;
    private final IUserService userService;
    private final IJobApplyLogService jobApplyLogService;
    private final IRPGFUNC irpgfunc;
    private final IJobMatchLogService jobMatchLogService;

    @Autowired
    public JobController(IJobService jobService, IUserService userService, IJobApplyLogService jobApplyLogService, IRPGFUNC irpgfunc, IJobMatchLogService jobMatchLogService) {
        this.jobService = jobService;
        this.userService = userService;
        this.jobApplyLogService = jobApplyLogService;
        this.irpgfunc = irpgfunc;
        this.jobMatchLogService = jobMatchLogService;
    }

    @ResponseBody
    @PostMapping("/publish")
    public Response CreateJob(@RequestBody CreateJobRequest request,
                              HttpServletRequest servletRequest) {
        String token = servletRequest.getHeader("token");
        User user = userService.buildUserByToken(token);
        request.setCreatedUserId(user.getUserId());
        return jobService.createJob(request);
    }

    @ResponseBody
    @PostMapping("/jobPlaza")
    public Response JobPlaza(@RequestBody Request request) {
        return jobService.buildJobs(request);
    }

    @ResponseBody
    @GetMapping("/{jobId}")
    public Response JobDetail(@PathVariable Integer jobId) {
        return jobService.buildJobInfo(jobId);
    }

    @ResponseBody
    @PostMapping("/applyJob")
    public Response JobApply(@RequestBody ApplyJobRequest applyJobRequest,
                             HttpServletRequest httpServletRequest) {
        Response response = new Response();

        //check token
        String token = httpServletRequest.getHeader("token");
        if (token == null) {
            //no token in header
            response.setErrorCode(10001);
            return response;
        }
        User user = userService.buildUserInfoByToken(token);
        if (user == null) {
            //token is incorrect
            response.setErrorCode(10004);
            return response;
        }
        applyJobRequest.setToken(token);

        //check job
        if (applyJobRequest.getJobId() == null) {
            //no job id
            response.setErrorCode(10002);
            return response;
        }
        Job job = jobService.loadJobInfoNoDetail(applyJobRequest.getJobId());
        if (job == null) {
            //no job
            response.setErrorCode(10005);
            return response;
        }

        if (job.getMatchLogId() != null) {
            //the job contracted already
            response.setErrorCode(10006);
            return response;
        }
        if (jobApplyLogService.isApplied(user.getUserId())) {
            //the job has applied by current user already
            response.setErrorCode(10007);
            return response;
        }

        try {
            jobService.applyJob(applyJobRequest);
        } catch (Exception ex) {
            response.setErrorCode(Integer.parseInt(ex.getMessage()));
            return response;
        }
        return response;
    }

    @ResponseBody
    @PostMapping("/loadJobToMatch")
    public Response loadJobToMatch(@RequestBody Request request,
                                   HttpServletRequest httpServletRequest) {
        /** todo
         * find by jobDao, job.matchLogId!=null
         * the page index and page size in the request body.
         */
        Response response = new Response();
        String token = httpServletRequest.getHeader("token");
        if (!irpgfunc.checkToken(token)) {
            response.setErrorCode(10004);
            return response;
        }
        Page<Job> jobPage = jobService.loadJobToMatch(request);
        response.setData(jobPage);

        return response;
    }

    @ResponseBody
    @PostMapping("/addNewJobMatchLog")
    public Response addNewJobMatchLog(@RequestBody Request request,
                                      HttpServletRequest httpServletRequest){
        Response response=new Response();
        String token=httpServletRequest.getHeader("token");
        if(!irpgfunc.checkToken(token)){
            response.setErrorCode(10004);
        }
        try{
            Map map =new HashMap();
            map.put("jobId",request.getJobId());
            map.put("matchUserId", request.getMatchUserId());
            jobMatchLogService.addNewMatchJobLog(map);
        }catch (Exception ex){
            response.setErrorCode(10011);
            return response;
        }
        return response;
    }
}

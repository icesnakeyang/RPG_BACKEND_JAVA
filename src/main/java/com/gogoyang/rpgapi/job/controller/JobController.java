package com.gogoyang.rpgapi.job.controller;

import com.gogoyang.rpgapi.constant.LogStatus;
import com.gogoyang.rpgapi.job.ENTITY.job.Job;
import com.gogoyang.rpgapi.job.job.vo.JobRequest;
import com.gogoyang.rpgapi.job.jobApply.entity.JobApply;
import com.gogoyang.rpgapi.job.jobApply.service.IJobApplyService;
import com.gogoyang.rpgapi.job.jobApply.vo.JobApplyRequest;
import com.gogoyang.rpgapi.job.jobMatch.entity.JobMatch;
import com.gogoyang.rpgapi.job.jobMatch.service.IJobMatchService;
import com.gogoyang.rpgapi.job.job.service.IJobService;
import com.gogoyang.rpgapi.user.userInfo.entity.UserInfo;
import com.gogoyang.rpgapi.user.userInfo.service.IUserInfoService;
import com.gogoyang.rpgapi.vo.Response;
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

    /**
     * 用户发布一条新任务
     * User publish a new job
     *
     * @param request
     * @param servletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/publish")
    public Response CreateJob(@RequestBody JobRequest request,
                              HttpServletRequest servletRequest) {
        Response response = new Response();
        try {
            String token = servletRequest.getHeader("token");
            UserInfo userInfo = iUserInfoService.checkToken(token);

            Job job = request.toJob();
            job.setCreatedUserId(userInfo.getUserId());
            job = iJobService.createJob(job);
            response.setData(job);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
                return response;
            } catch (Exception ex2) {
                response.setErrorCode(10032);
                return response;
            }
        }
        return response;
    }

    /**
     * 在任务广场显示一批目前正在等待交易的任务
     * read unassigned jobs to display in job plaza
     *
     * @return
     */
    @ResponseBody
    @GetMapping("/jobPlaza")
    public Response JobPlaza() {
        Response response = new Response();
        try {
            Page<Job> jobs = iJobService.loadJobUnMatch(0, 100);
            response.setData(jobs);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
                return response;
            } catch (Exception ex2) {
                response.setErrorCode(10026);
                return response;
            }
        }
        return response;
    }

    /**
     * 读取一条任务，包含详情
     * read a job with jobDetail
     *
     * @param jobId
     * @return
     */
    @ResponseBody
    @GetMapping("/{jobId}")
    public Response JobDetail(@PathVariable Integer jobId) {
        Response response = new Response();
        try {
            Job job = iJobService.loadJobByJobId(jobId);
            response.setData(job);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
                return response;
            } catch (Exception ex2) {
                response.setErrorCode(10026);
                return response;
            }
        }

        return response;
    }

    /**
     * 用户申请任务
     * Apply a job
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/applyJob")
    public Response JobApply(@RequestBody JobApplyRequest request,
                             HttpServletRequest httpServletRequest) {
        Response response = new Response();

        try {

            //check token
            String token = httpServletRequest.getHeader("token");
            UserInfo userInfo = iUserInfoService.checkToken(token);

            if (userInfo == null) {
                //no token in header
                response.setErrorCode(10004);
                return response;
            }

            //check job
            Job job = iJobService.loadJobByJobId(request.getJobId());
            if (job == null) {
                response.setErrorCode(10005);
                return response;
            }
            //是否已成交
            if (job.getStatus()==LogStatus.MATCHED) {
                response.setErrorCode(10006);
                return response;
            }
            //检查用户是否为甲方
            if(job.getCreatedUserId()==userInfo.getUserId()){
                response.setErrorCode(10037);
                return response;
            }

            //检查用户是否已经申请过该任务了
            if (iJobApplyService.isApplied(userInfo.getUserId(), request.getJobId())) {
                //the job has applied by current user already
                response.setErrorCode(10007);
                return response;
            }

            //保存任务申请日志
            JobApply jobApply = new JobApply();
            jobApply.setApplyTime(new Date());
            jobApply.setApplyUserId(userInfo.getUserId());
            jobApply.setJobId(request.getJobId());
            iJobApplyService.createJobApply(jobApply);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
                return response;
            } catch (Exception ex2) {
                response.setErrorCode(10033);
                return response;
            }
        }
        return response;
    }

    /**
     * 用户读取RPG秘书分配给自己的任务
     *
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @GetMapping("/loadMyNewJob")
    public Response loadMyNewJob(HttpServletRequest httpServletRequest) {
        Response response = new Response();
        try {
            String token = httpServletRequest.getHeader("token");
            UserInfo userInfo = iUserInfoService.checkToken(token);
            if (userInfo == null) {
                response.setErrorCode(10004);
            }

            ArrayList<JobMatch> newMatchs = new ArrayList<JobMatch>();
            newMatchs = iJobMatchService.loadMyNewJobMatch(userInfo.getUserId());
            ArrayList newJobs = new ArrayList();
            for (int i = 0; i < newMatchs.size(); i++) {
                Map map=new HashMap();
                Job job = iJobService.loadJobByJobIdTiny(newMatchs.get(i).getJobId());
                if(job!=null) {
                    map.put("match", newMatchs.get(i));
                    job.setCreatedUserName(iUserInfoService.getUserName(job.getCreatedUserId()));
                    job.setJobApplyNum(iJobApplyService.countApplyUsers(job.getJobId()));
                    job.setJobMatchNum(iJobMatchService.countMatchingUsers(job.getJobId()));
                    map.put("job", job);
                    newJobs.add(map);
                }
            }
            response.setData(newJobs);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            } catch (Exception ex2) {
                response.setErrorCode(10013);
            }
            return response;
        }
        return response;
    }

    /**
     * 用户读取自己申请的任务
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @GetMapping("/loadMyApplyJob")
    public Response loadMyApplyJob(HttpServletRequest httpServletRequest){
        Response response=new Response();
        try{
            String token=httpServletRequest.getHeader("token");
            UserInfo userInfo=iUserInfoService.loadUserByToken(token);
            if(userInfo==null){
                response.setErrorCode(10004);
                return response;
            }

            ArrayList jobList=iJobService.loadMyApplyJob(userInfo.getUserId());
            response.setData(jobList);

        }catch (Exception ex){
            try{
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
                return response;
            }catch (Exception ex2){
                response.setErrorCode(10026);
            }
        }
        return response;
    }
}

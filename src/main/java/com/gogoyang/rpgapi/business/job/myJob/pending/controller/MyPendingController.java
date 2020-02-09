package com.gogoyang.rpgapi.business.job.myJob.pending.controller;

import com.gogoyang.rpgapi.business.job.myJob.pending.service.IMyPendingBusinessService;
import com.gogoyang.rpgapi.business.job.vo.JobRequest;
import com.gogoyang.rpgapi.framework.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 我发布的任务，但没有成交的
 */
@RestController
@RequestMapping("/rpgapi/mypending")
public class MyPendingController {
    private final IMyPendingBusinessService iMyPendingBusinessService;

    @Autowired
    public MyPendingController(IMyPendingBusinessService iMyPendingBusinessService) {
        this.iMyPendingBusinessService = iMyPendingBusinessService;
    }

    /**
     * Read my published new task list
     * 读取我发布的等待分配中的新任务
     */
    @ResponseBody
    @PostMapping("/listMyPendingJob")
    public Response listMyPendingJob(@RequestBody JobRequest request,
            HttpServletRequest httpServletRequest){
        Response response=new Response();
        try {
            String token=httpServletRequest.getHeader("token");
            Map in =new HashMap();
            in.put("token", token);
            in.put("pageIndex", request.getPageIndex());
            in.put("pageSize", request.getPageSize());
            Map out=iMyPendingBusinessService.listMyPendingJob(in);
            response.setData(out);
        }catch (Exception ex){
            try{
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            }catch (Exception ex2){
                response.setErrorCode(10098);
            }
        }
        return response;
    }

    @ResponseBody
    @PostMapping("/getMyPendingJob")
    public Response getMyPendingJob(@RequestBody JobRequest request,
                                    HttpServletRequest httpServletRequest){
        Response response=new Response();
        try {
            String token=httpServletRequest.getHeader("token");
            Map in=new HashMap();
            in.put("token", token);
            in.put("jobId", request.getJobId());
            Map out=iMyPendingBusinessService.getMyPendingJob(in);
            response.setData(out);
        }catch (Exception ex){
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            }catch (Exception ex2){
                response.setErrorCode(10120);
            }
        }
        return response;
    }

    /**
     * Update the published but not matched common
     * 修改已经发布但未成交的任务
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/updatePendingJob")
    public Response updatePendingJob(@RequestBody JobRequest request,
                                     HttpServletRequest httpServletRequest){
        Response response=new Response();
        try {
            String token=httpServletRequest.getHeader("token");
            Map in=new HashMap();
            in.put("token", token);
            in.put("jobId", request.getJobId());
            in.put("title", request.getTitle());
            in.put("code", request.getCode());
            in.put("days", request.getDays());
            in.put("price", request.getPrice());
            in.put("jobDetail",request.getJobDetail());
            iMyPendingBusinessService.updateJob(in);
        }catch (Exception ex){
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            }catch (Exception ex2){
                response.setErrorCode(10099);
            }
        }
        return response;
    }

    /**
     * 删除我发布的未匹配任务
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/deletePendingJob")
    public Response deletePendingJob(@RequestBody JobRequest request,
                                     HttpServletRequest httpServletRequest){
        Response response=new Response();
        try {
            String token=httpServletRequest.getHeader("token");
            Map in=new HashMap();
            in.put("token", token);
            in.put("jobId", request.getJobId());
            iMyPendingBusinessService.deletePendingJob(in);
        }catch (Exception ex){
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            }catch (Exception ex2){
                response.setErrorCode(10103);
            }
        }
        return response;
    }
}

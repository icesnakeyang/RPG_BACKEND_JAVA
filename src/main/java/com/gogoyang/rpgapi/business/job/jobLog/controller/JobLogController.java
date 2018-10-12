package com.gogoyang.rpgapi.business.job.jobLog.controller;

import com.gogoyang.rpgapi.business.job.jobLog.service.IJobLogBusinessService;
import com.gogoyang.rpgapi.business.job.jobLog.vo.LogRequest;
import com.gogoyang.rpgapi.business.vo.Response;
import com.gogoyang.rpgapi.meta.log.entity.JobLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/job/log")
public class JobLogController {
    private final IJobLogBusinessService iJobLogBusinessService;

    @Autowired
    public JobLogController(IJobLogBusinessService iJobLogBusinessService) {
        this.iJobLogBusinessService = iJobLogBusinessService;
    }

    /**
     * 创建一个任务日志
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/createLog")
    public Response createLog(@RequestBody LogRequest request,
                              HttpServletRequest httpServletRequest){
        Response response=new Response();
        try {
            String token=httpServletRequest.getHeader("token");
            Map in =new HashMap();
            in.put("token", token);
            in.put("jobId", request.getJobId());
            in.put("content", request.getContent());

            iJobLogBusinessService.createLog(in);

        }catch (Exception ex){
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
                return response;
            }catch (Exception ex2){
                response.setErrorCode(10052);
                return response;
            }
        }
        return response;
    }

    /**
     * 读取一个任务的所有日志
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/jobLog")
    public Response loadJobLog(@RequestBody LogRequest request,
                               HttpServletRequest httpServletRequest){
        Response response=new Response();
        try {
            String token=httpServletRequest.getHeader("token");
            Map in=new HashMap();
            in.put("token", token);
            in.put("jobId", request.getJobId());
            in.put("pageIndex", request.getPageIndex());
            in.put("pageSize", request.getPageSize());
            Page<JobLog> jobLogs=iJobLogBusinessService.loadJobLog(in);
            response.setData(jobLogs);
        }catch (Exception ex){
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
                return response;
            }catch (Exception ex2){
                response.setErrorCode(10054);
                return response;
            }
        }
        return response;
    }

    /**
     * 设置所有我未阅读的任务的阅读时间
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/setJobLogReadTime")
    public Response setJobLogReadTime(@RequestBody LogRequest request,
                                      HttpServletRequest httpServletRequest){

        Response response=new Response();
        try {
            String token=httpServletRequest.getHeader("token");
            Integer jobId=request.getJobId();
            Map in=new HashMap();
            in.put("token", token);
            in.put("jobId", jobId);
            iJobLogBusinessService.setJobLogReadTime(in);
        }catch (Exception ex){
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
                return response;
            }catch (Exception ex2){
                response.setErrorCode(10055);
                return response;
            }
        }
        return response;
    }
}

package com.gogoyang.rpgapi.business.job.myJob.log.controller;

import com.gogoyang.rpgapi.business.job.myJob.log.service.IMyLogBusinessService;
import com.gogoyang.rpgapi.business.job.myJob.log.vo.LogRequest;
import com.gogoyang.rpgapi.framework.vo.Response;
import com.gogoyang.rpgapi.meta.log.entity.JobLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/rpgapi/job/log")
public class MyLogController {
    private final IMyLogBusinessService iMyLogBusinessService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public MyLogController(IMyLogBusinessService iMyLogBusinessService) {
        this.iMyLogBusinessService = iMyLogBusinessService;
    }

    /**
     * 创建一个任务日志
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/createLog")
    public Response createLog(@RequestBody LogRequest request,
                              HttpServletRequest httpServletRequest) {
        Response response = new Response();
        try {
            String token = httpServletRequest.getHeader("token");
            Map in = new HashMap();
            in.put("token", token);
            in.put("jobId", request.getJobId());
            in.put("content", request.getContent());

            iMyLogBusinessService.createLog(in);

        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
                return response;
            } catch (Exception ex2) {
                response.setErrorCode(10052);
                return response;
            }
        }
        return response;
    }

    /**
     * 读取一个任务的所有日志
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/jobLog")
    public Response loadJobLog(@RequestBody LogRequest request,
                               HttpServletRequest httpServletRequest) {
        Response response = new Response();
        try {
            String token = httpServletRequest.getHeader("token");
            Map in = new HashMap();
            in.put("token", token);
            in.put("jobId", request.getJobId());
            in.put("pageIndex", request.getPageIndex());
            in.put("pageSize", request.getPageSize());
            Page<JobLog> jobLogs = iMyLogBusinessService.loadJobLog(in);
            response.setData(jobLogs);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
                return response;
            } catch (Exception ex2) {
                response.setErrorCode(10054);
                return response;
            }
        }
        return response;
    }

    /**
     * 设置所有我未阅读的任务的阅读时间
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/setJobLogReadTime")
    public Response setJobLogReadTime(@RequestBody LogRequest request,
                                      HttpServletRequest httpServletRequest) {

        Response response = new Response();
        try {
            String token = httpServletRequest.getHeader("token");
            Integer jobId = request.getJobId();
            Map in = new HashMap();
            in.put("token", token);
            in.put("jobId", jobId);
            iMyLogBusinessService.setJobLogReadTime(in);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            } catch (Exception ex2) {
                response.setErrorCode(10055);
                logger.error(ex.getMessage());
            }
        }
        return response;
    }
}

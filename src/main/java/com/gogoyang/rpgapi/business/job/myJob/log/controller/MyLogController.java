package com.gogoyang.rpgapi.business.job.myJob.log.controller;

import com.gogoyang.rpgapi.business.job.myJob.log.service.IMyLogBusinessService;
import com.gogoyang.rpgapi.business.job.myJob.log.vo.LogRequest;
import com.gogoyang.rpgapi.framework.common.ICommonBusinessService;
import com.gogoyang.rpgapi.framework.constant.GogoActType;
import com.gogoyang.rpgapi.framework.constant.GogoStatus;
import com.gogoyang.rpgapi.framework.vo.Response;
import com.gogoyang.rpgapi.meta.log.entity.JobLog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/rpgapi/job/log")
public class MyLogController {
    private final IMyLogBusinessService iMyLogBusinessService;
    private final ICommonBusinessService iCommonBusinessService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public MyLogController(IMyLogBusinessService iMyLogBusinessService,
                           ICommonBusinessService iCommonBusinessService) {
        this.iMyLogBusinessService = iMyLogBusinessService;
        this.iCommonBusinessService = iCommonBusinessService;
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
        Map in=new HashMap();
        Map logMap=new HashMap();
        Map memoMap=new HashMap();
        try {
            String token = httpServletRequest.getHeader("token");
            in.put("token", token);
            in.put("jobId", request.getJobId());
            in.put("content", request.getContent());

            logMap.put("GogoActType", GogoActType.CREATE_JOB_LOG);
            logMap.put("token", token);
            memoMap.put("jobId", request.getJobId());
            iMyLogBusinessService.createLog(in);

            logMap.put("result", GogoStatus.SUCCESS);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            } catch (Exception ex2) {
                response.setErrorCode(30000);
                logger.error(ex.getMessage());
            }
            logMap.put("result", GogoStatus.FAIL);
            memoMap.put("error", ex.getMessage());
        }
        try {
            logMap.put("memo", memoMap);
            iCommonBusinessService.createUserActionLog(logMap);
        }catch (Exception ex3){
            logger.error(ex3.getMessage());
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
    @PostMapping("/listJobLog")
    public Response listJobLog(@RequestBody LogRequest request,
                               HttpServletRequest httpServletRequest) {
        Response response = new Response();
        Map in = new HashMap();
        Map logMap = new HashMap();
        Map memoMap = new HashMap();

        try {
            String token = httpServletRequest.getHeader("token");
            in.put("token", token);
            in.put("jobId", request.getJobId());
            in.put("pageIndex", request.getPageIndex());
            in.put("pageSize", request.getPageSize());

            logMap.put("GogoActType", GogoActType.LIST_JOB_LOG);
            logMap.put("token", token);
            memoMap.put("jobId", request.getJobId());
            ArrayList<JobLog> jobLogs = iMyLogBusinessService.loadJobLog(in);
            response.setData(jobLogs);
            logMap.put("result", GogoStatus.SUCCESS);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            } catch (Exception ex2) {
                response.setErrorCode(30000);
                logger.error(ex.getMessage());
            }
            logMap.put("result", GogoStatus.FAIL);
            memoMap.put("error", ex.getMessage());
        }
        try {
            logMap.put("memo", memoMap);
            iCommonBusinessService.createUserActionLog(logMap);
        } catch (Exception ex3) {
            logger.error(ex3.getMessage());
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
            String jobId = request.getJobId();
            Map in = new HashMap();
            in.put("token", token);
            in.put("jobId", jobId);
            iMyLogBusinessService.setJobLogReadTime(in);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            } catch (Exception ex2) {
                response.setErrorCode(30000);
                logger.error(ex.getMessage());
            }
        }
        return response;
    }
}

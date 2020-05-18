package com.gogoyang.rpgapi.business.job.myJob.apply.controller;

import com.gogoyang.rpgapi.business.job.myJob.apply.service.IMyApplyBusinessService;
import com.gogoyang.rpgapi.business.job.vo.JobRequest;
import com.gogoyang.rpgapi.framework.common.ICommonBusinessService;
import com.gogoyang.rpgapi.framework.constant.GogoActType;
import com.gogoyang.rpgapi.framework.constant.GogoStatus;
import com.gogoyang.rpgapi.framework.vo.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/rpgapi/job/myApply")
public class MyApplyController {
    private final IMyApplyBusinessService iMyApplyBusinessService;
    private final ICommonBusinessService iCommonBusinessService;

    private Logger logger= LoggerFactory.getLogger(getClass());

    @Autowired
    public MyApplyController(IMyApplyBusinessService iMyApplyBusinessService,
                             ICommonBusinessService iCommonBusinessService) {
        this.iMyApplyBusinessService = iMyApplyBusinessService;
        this.iCommonBusinessService = iCommonBusinessService;
    }

    /**
     * 读取所有我申请的任务，且未处理的。
     *
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/listMyApplyJob")
    public Response listMyApplyJob(@RequestBody JobRequest request,
                                   HttpServletRequest httpServletRequest) {
        Response response = new Response();
        try {
            Map in = new HashMap();
            String token = httpServletRequest.getHeader("token");
            in.put("token", token);
            in.put("pageIndex", request.getPageIndex());
            in.put("pageSize", request.getPageSize());

            Map out = new HashMap();
            out = iMyApplyBusinessService.listJobByMyApply(in);
            response.setData(out);
        } catch (Exception ex) {
            try {
                response.setData(Integer.parseInt(ex.getMessage()));
                return response;
            } catch (Exception ex2) {
                response.setErrorCode(10026);
                return response;
            }
        }
        return response;
    }

    @ResponseBody
    @PostMapping("/getMyApplyJob")
    public Response getMyApplyJob(@RequestBody JobRequest request,
                                  HttpServletRequest httpServletRequest) {
        Response response = new Response();
        try {
            String token = httpServletRequest.getHeader("token");
            Map in = new HashMap();
            in.put("token", token);
            in.put("jobId", request.getJobId());
            Map out = iMyApplyBusinessService.getMyApplyJob(in);
            response.setData(out);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            } catch (Exception ex2) {
                response.setErrorCode(10121);
            }
        }
        return response;
    }

    /**
     * 用户申请一个任务
     * Apply a common
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/applyJob")
    public Response applyJob(@RequestBody JobRequest request,
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

            logMap.put("GogoActType", GogoActType.APPLY_JOB);
            logMap.put("token", token);
            memoMap.put("jobId", request.getJobId());
            iMyApplyBusinessService.applyJob(in);
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
}

package com.gogoyang.rpgapi.business.job.myJob.pending.controller;

import com.gogoyang.rpgapi.business.job.myJob.pending.service.IMyPendingBusinessService;
import com.gogoyang.rpgapi.business.job.vo.JobRequest;
import com.gogoyang.rpgapi.framework.common.ICommonBusinessService;
import com.gogoyang.rpgapi.framework.common.IRPGFunction;
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

/**
 * 我发布的任务，但没有成交的
 */
@RestController
@RequestMapping("/rpgapi/mypending")
public class MyPendingController {
    private final IMyPendingBusinessService iMyPendingBusinessService;
    private final ICommonBusinessService iCommonBusinessService;
    private final IRPGFunction irpgFunction;

    private Logger logger= LoggerFactory.getLogger(getClass());

    @Autowired
    public MyPendingController(IMyPendingBusinessService iMyPendingBusinessService,
                               ICommonBusinessService iCommonBusinessService,
                               IRPGFunction irpgFunction) {
        this.iMyPendingBusinessService = iMyPendingBusinessService;
        this.iCommonBusinessService = iCommonBusinessService;
        this.irpgFunction = irpgFunction;
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
                response.setErrorCode(30000);
                logger.error(ex.getMessage());
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

        Map in=new HashMap();
        Map logMap=new HashMap();
        Map memoMap=new HashMap();
        try {
            String token=httpServletRequest.getHeader("token");
            in.put("token", token);
            in.put("jobId", request.getJobId());
            in.put("title", request.getTitle());
            in.put("code", request.getCode());
            in.put("days", request.getDays());
            in.put("price", request.getPrice());
            Map detailMap=irpgFunction.processRichTextPics(request.getJobDetail());
            in.put("detailMap",detailMap);

            logMap.put("GogoActType", GogoActType.UPDATE_JOB);
            logMap.put("token", token);
            logMap.put("jobId",request.getJobId());
            iMyPendingBusinessService.updateJob(in);
            memoMap.put("result", GogoStatus.SUCCESS);
        }catch (Exception ex){
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            }catch (Exception ex2){
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

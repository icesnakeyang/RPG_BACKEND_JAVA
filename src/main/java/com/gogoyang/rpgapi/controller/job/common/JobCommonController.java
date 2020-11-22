package com.gogoyang.rpgapi.controller.job.common;

import com.gogoyang.rpgapi.business.job.common.IJobCommonBusinessService;
import com.gogoyang.rpgapi.business.job.myJob.log.service.IMyLogBusinessService;
import com.gogoyang.rpgapi.business.job.myJob.log.vo.LogRequest;
import com.gogoyang.rpgapi.business.job.vo.JobRequest;
import com.gogoyang.rpgapi.framework.common.ICommonBusinessService;
import com.gogoyang.rpgapi.framework.constant.GogoActType;
import com.gogoyang.rpgapi.framework.constant.GogoStatus;
import com.gogoyang.rpgapi.framework.vo.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/rpgapi/job/common")
public class JobCommonController {
    private final IJobCommonBusinessService iJobCommonBusinessService;
    private final ICommonBusinessService iCommonBusinessService;
    private final IMyLogBusinessService iMyLogBusinessService;

    private Logger logger= LoggerFactory.getLogger(getClass());

    public JobCommonController(IJobCommonBusinessService iJobCommonBusinessService,
                               ICommonBusinessService iCommonBusinessService,
                               IMyLogBusinessService iMyLogBusinessService) {
        this.iJobCommonBusinessService = iJobCommonBusinessService;
        this.iCommonBusinessService = iCommonBusinessService;
        this.iMyLogBusinessService = iMyLogBusinessService;
    }

    /**
     * 统计我的所有任务未读信息
     * 包括所有任务的所有日志，完成，终止，申诉
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/totalMyUnread")
    public Response totalMyUnread(@RequestBody JobRequest request,
                                  HttpServletRequest httpServletRequest) {
        Response response = new Response();
        try {
            String token = httpServletRequest.getHeader("token");
            Map in = new HashMap();
            in.put("token", token);
            //如果有指定jobId，就统计该任务的未读信息
            in.put("jobId", request.getJobId());
            Map out = iJobCommonBusinessService.totalMyUnread(in);
            response.setData(out);
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

    /**
     * 读取一个job的简要信息
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/getJobTinyByJobId")
    public Response getJobTinyByJobId(@RequestBody JobRequest request,
                                      HttpServletRequest httpServletRequest) {
        Response response = new Response();
        Map in = new HashMap();
        try {
            String token=httpServletRequest.getHeader("token");
            in.put("token",token);
            in.put("jobId", request.getJobId());
            Map out = iJobCommonBusinessService.getJobTinyByJobId(in);
            response.setData(out);
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

    /**
     * 删除一个任务日志
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/deleteJobLog")
    public Response deleteJobLog(@RequestBody JobRequest request,
                                      HttpServletRequest httpServletRequest) {
        Response response = new Response();
        Map in = new HashMap();
        Map logMap=new HashMap();
        Map memoMap=new HashMap();
        try {
            String token=httpServletRequest.getHeader("token");
            in.put("token",token);
            in.put("jobLogId", request.getJobLogId());

            logMap.put("token", token);
            memoMap.put("jobLogId", request.getJobLogId());
            logMap.put("GogoActType", GogoActType.DELETE_JOB_TASK_LOG);
            iJobCommonBusinessService.deleteJobLog(in);

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
     * 读取一个任务日志详情
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/getJobLog")
    public Response getJobLog(@RequestBody LogRequest request,
                               HttpServletRequest httpServletRequest) {
        Response response = new Response();
        Map in = new HashMap();
        try {
            String token = httpServletRequest.getHeader("token");
            in.put("token", token);
            in.put("jobLogId", request.getJobLogId());

            Map out = iMyLogBusinessService.getJobLog(in);
            response.setData(out);
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

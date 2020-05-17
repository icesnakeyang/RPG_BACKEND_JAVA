package com.gogoyang.rpgapi.business.job.publicJob.controller;

import com.gogoyang.rpgapi.business.job.publicJob.service.IPublicJobBusinessService;
import com.gogoyang.rpgapi.business.job.publicJob.vo.PublicJobRequest;
import com.gogoyang.rpgapi.framework.common.ICommonBusinessService;
import com.gogoyang.rpgapi.framework.constant.GogoStatus;
import com.gogoyang.rpgapi.framework.vo.Response;
import com.gogoyang.rpgapi.framework.constant.GogoActType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/rpgapi/public_job")
public class PublicJobController {
    private final IPublicJobBusinessService iPublicJobBusinessService;
    private final ICommonBusinessService iCommonBusinessService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public PublicJobController(IPublicJobBusinessService iPublicJobBusinessService,
                               ICommonBusinessService iCommonBusinessService) {
        this.iPublicJobBusinessService = iPublicJobBusinessService;
        this.iCommonBusinessService = iCommonBusinessService;
    }

    @ResponseBody
    @PostMapping("/listPublicJob")
    public Response listPublicJob(@RequestBody PublicJobRequest request,
                                  HttpServletRequest httpServletRequest) {
        Response response = new Response();
        Map in = new HashMap();
        Map logMap = new HashMap();
        Map memoMap = new HashMap();
        try {
            String token = httpServletRequest.getHeader("token");
            in.put("pageIndex", request.getPageIndex());
            in.put("pageSize", request.getPageSize());
            logMap.put("token", token);
            logMap.put("GogoActType", GogoActType.LIST_PUBLIC_JOB);
            Map out = iPublicJobBusinessService.listPublicJob(in);
            response.setData(out);
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
     * 读取一条任务，包含详情
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @GetMapping("getJobDetail/{jobId}")
    public Response getJobDetail(@PathVariable Integer jobId,
                                  HttpServletRequest httpServletRequest) {
        Response response = new Response();
        Map in = new HashMap();
        Map logMap = new HashMap();
        Map memoMap = new HashMap();
        try {
            in.put("jobId", jobId);
            logMap.put("GogoActType", GogoActType.LOAD_JOB_DETAIL);
            memoMap.put("jobId", jobId);
            String token = httpServletRequest.getHeader("token");
            if (token != null) {
                memoMap.put("token", token);
            }
            Map out = iPublicJobBusinessService.getJobDetail(in);
            response.setData(out);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            } catch (Exception ex2) {
                response.setErrorCode(10026);
                logger.error(ex.getMessage());
            }
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
}

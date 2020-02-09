package com.gogoyang.rpgapi.business.job.myJob.common.controller;

import com.gogoyang.rpgapi.business.job.myJob.common.service.IMyJobCommonBusinessService;
import com.gogoyang.rpgapi.business.job.vo.JobRequest;
import com.gogoyang.rpgapi.controller.vo.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/job/my_common")
public class MyJobCommonController {
    private final IMyJobCommonBusinessService iMyJobCommonBusinessService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public MyJobCommonController(IMyJobCommonBusinessService iMyJobCommonBusinessService) {
        this.iMyJobCommonBusinessService = iMyJobCommonBusinessService;
    }

    @ResponseBody
    @PostMapping("/totalMyUnread")
    public Response totalMyUnread(@RequestBody JobRequest request,
                                  HttpServletRequest httpServletRequest) {
        Response response = new Response();
        try {
            String token = httpServletRequest.getHeader("token");
            Map in = new HashMap();
            in.put("token", token);
            in.put("jobId", request.getJobId());
            Map out = iMyJobCommonBusinessService.totalMyUnread(in);
            response.setData(out);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            } catch (Exception ex2) {
                System.out.println(ex.getMessage());
                response.setErrorCode(10119);
            }
        }
        return response;
    }

    @ResponseBody
    @PostMapping("/getJobTinyByTaskId")
    public Response getJobTinyByTaskId(@RequestBody JobRequest request,
                                       HttpServletRequest httpServletRequest) {
        Response response = new Response();
        try {
            Map in = new HashMap();
            Map out = iMyJobCommonBusinessService.getJobTinyByTaskId(in);
            response.setData(out);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            } catch (Exception ex2) {
                response.setErrorCode(10119);
            }
        }
        return response;
    }

    @ResponseBody
    @PostMapping("/getJobTinyByJobId")
    public Response getJobTinyByJobId(@RequestBody JobRequest request,
                                      HttpServletRequest httpServletRequest) {
        Response response = new Response();
        try {
            Map in = new HashMap();
            in.put("jobId", request.getJobId());
            Map out = iMyJobCommonBusinessService.getJobTinyByJobId(in);
            response.setData(out);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            } catch (Exception ex2) {
                response.setErrorCode(10119);
            }
        }
        return response;
    }


    @ResponseBody
    @PostMapping("/totalUnreadByJobId")
    public Response totalUnreadByJobId(@RequestBody JobRequest request,
                                      HttpServletRequest httpServletRequest) {
        Response response = new Response();
        Map in = new HashMap();
        try {
            String token = httpServletRequest.getHeader("token");
            in.put("token", token);
            in.put("jobId", request.getJobId());
            Map out = iMyJobCommonBusinessService.totalUnreadByJobId(in);
            response.setData(out);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            } catch (Exception ex2) {
                response.setErrorCode(10119);
                logger.error(ex.getMessage());
            }
        }
        return response;
    }

}

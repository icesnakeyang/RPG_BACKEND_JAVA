package com.gogoyang.rpgapi.business.job.myJob.common.controller;

import com.gogoyang.rpgapi.business.job.myJob.common.service.IMyJobCommonBusinessService;
import com.gogoyang.rpgapi.business.job.vo.JobRequest;
import com.gogoyang.rpgapi.business.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/job/my_common")
public class MyJobCommonController {
    private final IMyJobCommonBusinessService iMyJobCommonBusinessService;

    @Autowired
    public MyJobCommonController(IMyJobCommonBusinessService iMyJobCommonBusinessService) {
        this.iMyJobCommonBusinessService = iMyJobCommonBusinessService;
    }

    @ResponseBody
    @PostMapping("/totalMyUnread")
    public Response totalMyUnread(@RequestBody JobRequest request,
                                      HttpServletRequest httpServletRequest){
        Response response=new Response();
        try {
            String token=httpServletRequest.getHeader("token");
            Map in=new HashMap();
            in.put("token", token);
            in.put("jobId", request.getJobId());
            Map out=iMyJobCommonBusinessService.totalMyUnread(in);
            response.setData(out);
        }catch (Exception ex){
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            }catch (Exception ex2){
                response.setErrorCode(10119);
            }
        }
        return response;
    }

    @ResponseBody
    @PostMapping("/getJobTinyByTaskId")
    public Response getJobTinyByTaskId(@RequestBody JobRequest request,
                                      HttpServletRequest httpServletRequest){
        Response response=new Response();
        try {
            Map in=new HashMap();
            Map out=iMyJobCommonBusinessService.getJobTinyByTaskId(in);
            response.setData(out);
        }catch (Exception ex){
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            }catch (Exception ex2){
                response.setErrorCode(10119);
            }
        }
        return response;
    }

    @ResponseBody
    @PostMapping("/getJobTinyByJobId")
    public Response getJobTinyByJobId(@RequestBody JobRequest request,
                                      HttpServletRequest httpServletRequest){
        Response response=new Response();
        try {
            Map in=new HashMap();
            in.put("jobId", request.getJobId());
            Map out=iMyJobCommonBusinessService.getJobTinyByJobId(in);
            response.setData(out);
        }catch (Exception ex){
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            }catch (Exception ex2){
                response.setErrorCode(10119);
            }
        }
        return response;
    }

}

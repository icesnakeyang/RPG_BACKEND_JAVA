package com.gogoyang.rpgapi.business.job.jobLog.controller;

import com.gogoyang.rpgapi.business.job.jobLog.service.IJobLogBusinessService;
import com.gogoyang.rpgapi.business.job.jobLog.vo.LogRequest;
import com.gogoyang.rpgapi.business.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
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
                response.setErrorCode(10051);
                return response;
            }
        }
        return response;
    }
}

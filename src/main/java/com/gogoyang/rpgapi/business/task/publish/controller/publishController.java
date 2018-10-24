package com.gogoyang.rpgapi.business.task.publish.controller;


import com.gogoyang.rpgapi.business.task.publish.service.IPublishBusinessService;
import com.gogoyang.rpgapi.business.job.vo.JobRequest;
import com.gogoyang.rpgapi.business.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/job/publish")
public class publishController {
    private final IPublishBusinessService iPublishBusinessService;

    @Autowired
    public publishController(IPublishBusinessService iPublishBusinessService) {
        this.iPublishBusinessService = iPublishBusinessService;
    }

    @ResponseBody
    @PostMapping("/publishNewJob")
    public Response publishNewJob(@RequestBody JobRequest request,
                                  HttpServletRequest httpServletRequest) {
        Response response = new Response();
        try {
            Map in = new HashMap();
            String token = httpServletRequest.getHeader("token");
            in.put("token", token);
            in.put("code", request.getCode());
            in.put("days", request.getDays());
            in.put("detail", request.getJobDetail());
            in.put("price", request.getPrice());
            in.put("taskId", request.getTaskId());
            in.put("title", request.getTitle());

            Map out = iPublishBusinessService.publishJob(in);
            response.setData(out);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
                return response;
            } catch (Exception ex2) {
                response.setErrorCode(10032);
                return response;
            }
        }
        return response;
    }
}

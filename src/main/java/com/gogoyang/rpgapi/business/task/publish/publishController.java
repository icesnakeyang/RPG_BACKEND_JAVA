package com.gogoyang.rpgapi.business.task.publish;


import com.gogoyang.rpgapi.meta.job.entity.Job;
import com.gogoyang.rpgapi.business.job.vo.JobRequest;
import com.gogoyang.rpgapi.meta.user.userInfo.entity.UserInfo;
import com.gogoyang.rpgapi.meta.user.userInfo.service.IUserInfoService;
import com.gogoyang.rpgapi.business.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/job/publish")
public class publishController {
    private final IUserInfoService iUserInfoService;
    private final IPublishJobService iPublishJobService;

    @Autowired
    public publishController(IUserInfoService iUserInfoService, IPublishJobService iPublishJobService) {
        this.iUserInfoService = iUserInfoService;
        this.iPublishJobService = iPublishJobService;
    }

    @ResponseBody
    @PostMapping("/publishNewJob")
    public Response publishNewJob(@RequestBody JobRequest request,
                                  HttpServletRequest httpServletRequest) {
        Response response = new Response();
        try {
            String token = httpServletRequest.getHeader("token");
            UserInfo userInfo = iUserInfoService.checkToken(token);
            if (userInfo == null) {
                response.setErrorCode(10004);
                return response;
            }

            Map in = new HashMap();

            in.put("userId", userInfo.getUserId());
            in.put("code", request.getCode());
            in.put("days", request.getDays());
            in.put("detail", request.getJobDetail());
            in.put("price", request.getPrice());
            in.put("taskId", request.getTaskId());
            in.put("title", request.getTitle());

            Job job = iPublishJobService.publishJob(in);
            response.setData(job);
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

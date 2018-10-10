package com.gogoyang.rpgapi.business.job.myMatch;

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
@RequestMapping("/job")
public class myNewJobController {
    private final IMyNewJobService iMyNewJobService;
    private final IUserInfoService iUserInfoService;

    @Autowired
    public myNewJobController(IMyNewJobService iMyNewJobService, IUserInfoService iUserInfoService) {
        this.iMyNewJobService = iMyNewJobService;
        this.iUserInfoService = iUserInfoService;
    }

    /**
     * 接受新任务
     * Accept new job
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/acceptNewJob")
    public Response acceptNewJob(@RequestBody JobRequest request,
                                 HttpServletRequest httpServletRequest){
        Response response=new Response();
        try {
            String token=httpServletRequest.getHeader("token");
            UserInfo userInfo=iUserInfoService.loadUserByToken(token);
            if(userInfo==null){
                response.setErrorCode(10004);
                return response;
            }
            Map in=new HashMap();
            in.put("jobId", request.getJobId());
            in.put("userId", userInfo.getUserId());
            iMyNewJobService.acceptNewJob(in);
        }catch (Exception ex){
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
                return response;
            }catch (Exception ex2){
                response.setErrorCode(10041);
                return response;
            }
        }
        return response;
    }
}

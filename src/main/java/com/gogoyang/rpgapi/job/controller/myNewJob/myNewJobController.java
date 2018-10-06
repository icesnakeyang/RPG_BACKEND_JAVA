package com.gogoyang.rpgapi.job.controller.myNewJob;

import com.gogoyang.rpgapi.job.meta.job.vo.JobRequest;
import com.gogoyang.rpgapi.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/job")
public class myNewJobController {
    private final IMyNewJobService iMyNewJobService;

    @Autowired
    public myNewJobController(IMyNewJobService iMyNewJobService) {
        this.iMyNewJobService = iMyNewJobService;
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
            Map in=new HashMap();
            in.put("jobMatchId", request.getJobMatchId());
            in.put("userId", request.getUserId());
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

package com.gogoyang.rpgapi.business.job.myPending.controller;

import com.gogoyang.rpgapi.business.job.myPending.service.IMyPendingBusinessService;
import com.gogoyang.rpgapi.business.job.vo.JobRequest;
import com.gogoyang.rpgapi.business.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 我发布的任务，但没有成交的
 */
@RestController
@RequestMapping("/mypending")
public class MyPendingController {
    private final IMyPendingBusinessService iMyPendingBusinessService;

    @Autowired
    public MyPendingController(IMyPendingBusinessService iMyPendingBusinessService) {
        this.iMyPendingBusinessService = iMyPendingBusinessService;
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
                response.setErrorCode(10098);
            }
        }
        return response;
    }
}

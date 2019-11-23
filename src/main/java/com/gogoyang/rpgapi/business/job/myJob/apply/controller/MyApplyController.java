package com.gogoyang.rpgapi.business.job.myJob.apply.controller;

import com.gogoyang.rpgapi.business.job.myJob.apply.service.IMyApplyBusinessService;
import com.gogoyang.rpgapi.business.job.vo.JobRequest;
import com.gogoyang.rpgapi.business.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/job/myApply")
public class MyApplyController {
    private final IMyApplyBusinessService iMyApplyBusinessService;

    @Autowired
    public MyApplyController(IMyApplyBusinessService iMyApplyBusinessService) {
        this.iMyApplyBusinessService = iMyApplyBusinessService;
    }

    /**
     * 读取所有我申请的任务，且未处理的。
     *
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/listMyApplyJob")
    public Response listMyApplyJob(@RequestBody JobRequest request,
                                   HttpServletRequest httpServletRequest) {
        Response response = new Response();
        try {
            Map in = new HashMap();
            String token = httpServletRequest.getHeader("token");
            in.put("token", token);
            in.put("pageIndex", request.getPageIndex());
            in.put("pageSize", request.getPageSize());

            Map out = new HashMap();
            out = iMyApplyBusinessService.listJobByMyApply(in);
            response.setData(out);
        } catch (Exception ex) {
            try {
                response.setData(Integer.parseInt(ex.getMessage()));
                return response;
            } catch (Exception ex2) {
                response.setErrorCode(10026);
                return response;
            }
        }
        return response;
    }

    @ResponseBody
    @PostMapping("/getMyApplyJob")
    public Response getMyApplyJob(@RequestBody JobRequest request,
                                  HttpServletRequest httpServletRequest) {
        Response response = new Response();
        try {
            String token = httpServletRequest.getHeader("token");
            Map in = new HashMap();
            in.put("token", token);
            in.put("jobId", request.getJobId());
            Map out = iMyApplyBusinessService.getMyApplyJob(in);
            response.setData(out);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            } catch (Exception ex2) {
                response.setErrorCode(10121);
            }
        }
        return response;
    }

    /**
     * 用户申请一个任务
     * Apply a common
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/applyJob")
    public Response applyJob(@RequestBody JobRequest request,
                             HttpServletRequest httpServletRequest) {
        Response response = new Response();
        try {
            String token = httpServletRequest.getHeader("token");
            Map in = new HashMap();
            in.put("token", token);
            in.put("jobId", request.getJobId());
            in.put("content", request.getContent());
            iMyApplyBusinessService.applyJob(in);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
                return response;
            } catch (Exception ex2) {
                response.setErrorCode(10033);
                return response;
            }
        }
        return response;
    }
}

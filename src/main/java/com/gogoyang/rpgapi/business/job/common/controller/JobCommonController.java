package com.gogoyang.rpgapi.business.job.common.controller;

import com.gogoyang.rpgapi.business.job.common.service.IJobCommonBusinessService;
import com.gogoyang.rpgapi.business.job.vo.JobRequest;
import com.gogoyang.rpgapi.business.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/job/public_common")
public class JobCommonController {
    private final IJobCommonBusinessService iJobCommonBusinessService;

    @Autowired
    public JobCommonController(IJobCommonBusinessService iJobCommonBusinessService) {
        this.iJobCommonBusinessService = iJobCommonBusinessService;
    }

    @ResponseBody
    @PostMapping("/publishNewJob")
    public Response publishNewJob(@RequestBody JobRequest request,
                                  HttpServletRequest httpServletRequest){
        Response response=new Response();
        try {
            String token=httpServletRequest.getHeader("token");
            Map in=new HashMap();
            in.put("token", token);
            in.put("code", request.getCode());
            in.put("days", request.getDays());
            in.put("detail", request.getJobDetail());
            in.put("price", request.getPrice());
            in.put("taskId", request.getTaskId());
            in.put("title", request.getTitle());
            iJobCommonBusinessService.publishNewJob(in);
        }catch (Exception ex){
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            }catch (Exception ex2){
                response.setErrorCode(10032);
            }
        }
        return response;
    }

    @ResponseBody
    @PostMapping("/listPublicJob")
    public Response listPublicJob(@RequestBody JobRequest request){
        Response response=new Response();
        try {
            Map in=new HashMap();
            in.put("pageIndex", request.getPageIndex());
            in.put("pageSize", request.getPageSize());
            Map out=iJobCommonBusinessService.listPublicJob(in);
            response.setData(out);
        }catch (Exception ex){
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            }catch (Exception ex2){
                response.setErrorCode(10026);
            }
        }
        return response;
    }

    /**
     * 读取一条任务，包含详情
     * read a common with jobDetail
     *
     * @param jobId
     * @return
     */
    @ResponseBody
    @GetMapping("/{jobId}")
    public Response loadJobDetail(@PathVariable Integer jobId) {
        Response response = new Response();
        try {
            Map in=new HashMap();
            in.put("jobId", jobId);
            Map out=iJobCommonBusinessService.getJobDetail(in);
            response.setData(out);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
                return response;
            } catch (Exception ex2) {
                response.setErrorCode(10026);
                return response;
            }
        }

        return response;
    }

    /**
     * 读取一条任务的简要信息，不包括detail
     * read a common without jobDetail
     *
     * @param jobId
     * @return
     */
    @ResponseBody
    @GetMapping("/tiny/{jobId}")
    public Response loadJobTiny(@PathVariable Integer jobId) {
        Response response = new Response();
        try {
            Map in=new HashMap();
            in.put("jobId", jobId);
            Map out=iJobCommonBusinessService.getJobTiny(in);
            response.setData(out);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
                return response;
            } catch (Exception ex2) {
                response.setErrorCode(10026);
                return response;
            }
        }

        return response;
    }
}

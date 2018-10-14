package com.gogoyang.rpgapi.business.job.detail.controller;

import com.gogoyang.rpgapi.business.job.detail.service.IJobDetailBusinessService;
import com.gogoyang.rpgapi.business.job.vo.JobRequest;
import com.gogoyang.rpgapi.business.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/job/detail")
public class JobDetailController {
    private final IJobDetailBusinessService iJobDetailBusinessService;

    @Autowired
    public JobDetailController(IJobDetailBusinessService iJobDetailBusinessService) {
        this.iJobDetailBusinessService = iJobDetailBusinessService;
    }

    /**
     * 读取一条任务，包含详情
     * read a job with jobDetail
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
            Map out=iJobDetailBusinessService.loadJobDetail(in);
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
     * 读取完整的任务信息，包括未读日志等
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/jobDeailPage")
    public Response loadJobDetailLogin(@RequestBody JobRequest request,
                                       HttpServletRequest httpServletRequest){
        Response response=new Response();
        try
        {
            String token=httpServletRequest.getHeader("token");
            Map in=new HashMap();
            in.put("token", token);
            in.put("jobId", request.getJobId());
            Map out=iJobDetailBusinessService.loadJobDetailLogin(in);
            response.setData(out);
        }catch (Exception ex){
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
                return response;
            }catch (Exception ex2){
                response.setErrorCode(10026);
                return response;
            }
        }
        return response;
    }
}

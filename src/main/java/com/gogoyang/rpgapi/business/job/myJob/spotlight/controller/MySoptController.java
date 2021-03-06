package com.gogoyang.rpgapi.business.job.myJob.spotlight.controller;

import com.gogoyang.rpgapi.business.job.myJob.spotlight.service.IMySpotBusinessService;
import com.gogoyang.rpgapi.business.job.vo.JobRequest;
import com.gogoyang.rpgapi.business.spotlight.vo.SpotlightRequest;
import com.gogoyang.rpgapi.framework.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/rpgapi/job/spotlight")
public class MySoptController {
    private final IMySpotBusinessService iMySpotBusinessService;

    @Autowired
    public MySoptController(IMySpotBusinessService iMySpotBusinessService) {
        this.iMySpotBusinessService = iMySpotBusinessService;
    }

    /**
     * 创建一个申诉事件
     * Create a spotlight
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/createSpotlight")
    public Response createSpotlight(@RequestBody SpotlightRequest request,
                               HttpServletRequest httpServletRequest) {
        Response response = new Response();
        try {
            String token = httpServletRequest.getHeader("token");
            Map in = new HashMap();
            in.put("token", token);
            in.put("content", request.getContent());
            in.put("jobId", request.getJobId());
            in.put("title", request.getTitle());
            iMySpotBusinessService.createSpotlight(in);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
                return response;
            } catch (Exception ex2) {
                response.setErrorCode(10076);
                return response;
            }
        }
        return response;
    }

    /**
     * 读取我的所有申诉事件
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/listMySpotlight")
    public Response listMySpotlight(@RequestBody JobRequest request,
                                    HttpServletRequest httpServletRequest) {
        Response response = new Response();
        try {
            String token = httpServletRequest.getHeader("token");
            Map in = new HashMap();
            in.put("token", token);
            in.put("jobId", request.getJobId());
            in.put("pageIndex", request.getPageIndex());
            in.put("pageSize", request.getPageSize());
            Map out = iMySpotBusinessService.listMySpotlight(in);
            response.setData(out);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
                return response;
            } catch (Exception ex2) {
                response.setErrorCode(10088);
                return response;
            }
        }
        return response;
    }
}

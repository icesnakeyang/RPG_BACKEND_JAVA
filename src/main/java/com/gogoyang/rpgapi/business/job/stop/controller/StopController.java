package com.gogoyang.rpgapi.business.job.stop.controller;

import com.gogoyang.rpgapi.business.job.stop.service.IStopBusinessService;
import com.gogoyang.rpgapi.business.job.stop.vo.StopRequest;
import com.gogoyang.rpgapi.business.vo.Response;
import com.gogoyang.rpgapi.meta.stop.entity.JobStop;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/job/stop")
public class StopController {
    private final IStopBusinessService iStopBusinessService;

    @Autowired
    public StopController(IStopBusinessService iStopBusinessService) {
        this.iStopBusinessService = iStopBusinessService;
    }

    /**
     * 创建一个新的终止任务申请
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/stopJob")
    public Response createComplete(@RequestBody StopRequest request,
                                   HttpServletRequest httpServletRequest) {
        Response response = new Response();
        try {
            String token = httpServletRequest.getHeader("token");
            Map in = new HashMap();
            in.put("token", token);
            in.put("jobId", request.getJobId());
            in.put("content", request.getContent());
            in.put("refund", request.getRefund());
            iStopBusinessService.createJobStop(in);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
                return response;
            } catch (Exception ex2) {
                response.setErrorCode(10064);
                return response;
            }
        }
        return response;
    }

    /**
     * 读取一个任务的所有终止申请
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/loadStopList")
    public Response loadCompleteList(@RequestBody StopRequest request,
                                     HttpServletRequest httpServletRequest) {
        Response response = new Response();
        try {
            String token = httpServletRequest.getHeader("token");
            Map in = new HashMap();
            in.put("token", token);
            in.put("jobId", request.getJobId());
            in.put("pageIndex", request.getPageIndex());
            in.put("pageSize", request.getPageSize());
            Page<JobStop> out = iStopBusinessService.loadStopList(in);
            response.setData(out);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
                return response;
            } catch (Exception ex2) {
                response.setErrorCode(10065);
                return response;
            }
        }
        return response;
    }

    /**
     * 设置所有我未阅读的终止申请的阅读时间
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/setStopReadTime")
    public Response setCompleteReadTime(@RequestBody StopRequest request,
                                        HttpServletRequest httpServletRequest) {

        Response response = new Response();
        try {
            String token = httpServletRequest.getHeader("token");
            Integer jobId = request.getJobId();
            Map in = new HashMap();
            in.put("token", token);
            in.put("jobId", jobId);
            iStopBusinessService.setStopReadTime(in);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
                return response;
            } catch (Exception ex2) {
                response.setErrorCode(10066);
                return response;
            }
        }
        return response;
    }

    /**
     * 拒绝终止任务申请
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("rejectStop")
    public Response rejectComplete(@RequestBody StopRequest request,
                                   HttpServletRequest httpServletRequest) {
        Response response = new Response();
        try {
            String token = httpServletRequest.getHeader("token");
            Map in = new HashMap();
            in.put("token", token);
            in.put("jobId", request.getJobId());
            in.put("processRemark", request.getProcessRemark());
            iStopBusinessService.rejectStop(in);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
                return response;

            } catch (Exception ex2) {
                response.setErrorCode(10067);
                return response;
            }
        }
        return response;
    }

    /**
     * 同意终止任务
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("acceptStop")
    public Response acceptComplete(@RequestBody StopRequest request,
                                   HttpServletRequest httpServletRequest) {
        Response response = new Response();
        try {
            String token = httpServletRequest.getHeader("token");
            Map in = new HashMap();
            in.put("token", token);
            in.put("jobId", request.getJobId());
            iStopBusinessService.acceptStop(in);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
                return response;

            } catch (Exception ex2) {
                response.setErrorCode(10068);
                return response;
            }
        }
        return response;
    }
}

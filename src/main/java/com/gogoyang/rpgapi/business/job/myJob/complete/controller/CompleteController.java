package com.gogoyang.rpgapi.business.job.myJob.complete.controller;

import com.gogoyang.rpgapi.business.job.myJob.complete.service.ICompleteBusinessService;
import com.gogoyang.rpgapi.business.job.myJob.complete.vo.JobCompleteRequest;
import com.gogoyang.rpgapi.business.job.vo.JobRequest;
import com.gogoyang.rpgapi.framework.vo.Response;
import com.gogoyang.rpgapi.meta.complete.entity.JobComplete;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/job/complete")
public class CompleteController {
    private final ICompleteBusinessService iCompleteBusinessService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public CompleteController(ICompleteBusinessService iCompleteBusinessService) {
        this.iCompleteBusinessService = iCompleteBusinessService;
    }

    /**
     * 创建一个新的验收任务日志
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/createComplete")
    public Response createComplete(@RequestBody JobCompleteRequest request,
                                   HttpServletRequest httpServletRequest) {
        Response response = new Response();
        try {
            String token = httpServletRequest.getHeader("token");
            Map in = new HashMap();
            in.put("token", token);
            in.put("jobId", request.getJobId());
            in.put("content", request.getContent());
            iCompleteBusinessService.createJobComplete(in);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
                return response;
            } catch (Exception ex2) {
                response.setErrorCode(10057);
                return response;
            }
        }
        return response;
    }

    /**
     * 读取一个任务的所有验收日志
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/listMyComplete")
    public Response listMyComplete(@RequestBody JobRequest request,
                                   HttpServletRequest httpServletRequest) {
        Response response = new Response();
        try {
            String token = httpServletRequest.getHeader("token");
            Map in = new HashMap();
            in.put("token", token);
            in.put("jobId", request.getJobId());
            in.put("pageIndex", request.getPageIndex());
            in.put("pageSize", request.getPageSize());
            Page<JobComplete> out = iCompleteBusinessService.listMyComplete(in);
            response.setData(out);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
                return response;
            } catch (Exception ex2) {
                response.setErrorCode(10056);
                return response;
            }
        }
        return response;
    }

    /**
     * 设置所有我未阅读的验收任务的阅读时间
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/setCompleteReadTime")
    public Response setCompleteReadTime(@RequestBody JobCompleteRequest request,
                                        HttpServletRequest httpServletRequest) {

        Response response = new Response();
        try {
            String token = httpServletRequest.getHeader("token");
            Integer jobId = request.getJobId();
            Map in = new HashMap();
            in.put("token", token);
            in.put("jobId", jobId);
            iCompleteBusinessService.setCompleteReadTime(in);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            } catch (Exception ex2) {
                response.setErrorCode(10058);
                logger.error(ex.getMessage());
            }
        }
        return response;
    }

    /**
     * 拒绝乙方提交的任务验收申请
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("rejectComplete")
    public Response rejectComplete(@RequestBody JobCompleteRequest request,
                                   HttpServletRequest httpServletRequest) {
        Response response = new Response();
        try {
            String token = httpServletRequest.getHeader("token");
            Map in = new HashMap();
            in.put("token", token);
            in.put("jobId", request.getJobId());
            in.put("processRemark", request.getProcessRemark());
            iCompleteBusinessService.rejectComplete(in);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
                return response;

            } catch (Exception ex2) {
                response.setErrorCode(10060);
                return response;
            }
        }
        return response;
    }

    /**
     * 通过乙方提交的任务验收申请
     * 如果乙方没有提交申请，则直接通过任务验收
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("acceptComplete")
    public Response acceptComplete(@RequestBody JobCompleteRequest request,
                                   HttpServletRequest httpServletRequest) {
        Response response = new Response();
        try {
            String token = httpServletRequest.getHeader("token");
            Map in = new HashMap();
            in.put("token", token);
            in.put("jobId", request.getJobId());
            in.put("processRemark", request.getProcessRemark());
            iCompleteBusinessService.acceptComplete(in);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
                return response;

            } catch (Exception ex2) {
                response.setErrorCode(10061);
                return response;
            }
        }
        return response;
    }

    @ResponseBody
    @PostMapping("/listMyPartyAAcceptJob")
    public Response listMyPartyAAcceptJob(@RequestBody JobCompleteRequest request,
                                          HttpServletRequest httpServletRequest) {
        Response response = new Response();
        try {
            String token = httpServletRequest.getHeader("token");
            Map in = new HashMap();
            in.put("token", token);
            in.put("pageIndex", request.getPageIndex());
            in.put("pageSize", request.getPageSize());
            Map out = iCompleteBusinessService.listMyPartyAAcceptJob(in);
            response.setData(out);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            } catch (Exception ex2) {
                response.setErrorCode(10132);
            }
        }
        return response;
    }

    @ResponseBody
    @PostMapping("/listMyPartyBAcceptJob")
    public Response listMyPartyBAcceptJob(@RequestBody JobCompleteRequest request,
                                          HttpServletRequest httpServletRequest) {
        Response response = new Response();
        try {
            String token = httpServletRequest.getHeader("token");
            Map in = new HashMap();
            in.put("token", token);
            in.put("pageIndex", request.getPageIndex());
            in.put("pageSize", request.getPageSize());
            Map out = iCompleteBusinessService.listMyPartyBAcceptJob(in);
            response.setData(out);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            } catch (Exception ex2) {
                response.setErrorCode(10132);
            }
        }
        return response;
    }

    @ResponseBody
    @PostMapping("/setAcceptReadTime")
    public Response setAcceptReadTime(@RequestBody JobCompleteRequest request,
                                      HttpServletRequest httpServletRequest) {
        Response response = new Response();
        try {
            String token = httpServletRequest.getHeader("token");
            Map in = new HashMap();
            in.put("token", token);
            iCompleteBusinessService.setAcceptReadTime(in);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            } catch (Exception ex2) {
                response.setErrorCode(10058);
            }
        }
        return response;
    }
}

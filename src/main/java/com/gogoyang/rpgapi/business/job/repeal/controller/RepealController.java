package com.gogoyang.rpgapi.business.job.repeal.controller;

import com.gogoyang.rpgapi.business.job.repeal.service.IRepealBusinessService;
import com.gogoyang.rpgapi.business.job.repeal.vo.RepealRequest;
import com.gogoyang.rpgapi.business.vo.Response;
import com.gogoyang.rpgapi.meta.repeal.entity.Repeal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("job/repeal")
public class RepealController {
    private final IRepealBusinessService iRepealBusinessService;

    @Autowired
    public RepealController(IRepealBusinessService iRepealBusinessService) {
        this.iRepealBusinessService = iRepealBusinessService;
    }

    /**
     * 创建一个撤销申诉申请
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/create")
    public Response createRepeal(@RequestBody RepealRequest request,
                                 HttpServletRequest httpServletRequest) {
        Response response = new Response();
        try {
            String token = httpServletRequest.getHeader("token");
            Map in = new HashMap();
            in.put("token", token);
            in.put("content", request.getContent().toString());
            in.put("jobId", request.getJobId().toString());
            in.put("spotId", request.getSpotId());
            Repeal repeal = iRepealBusinessService.createRepeal(in);
            response.setData(repeal);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
                return response;
            } catch (Exception ex2) {
                response.setErrorCode(10079);
                return response;
            }
        }
        return response;
    }

    /**
     * 读取一个任务的所有撤销申诉申请记录
     * 包括已经处理的，和未处理的
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/list")
    public Response listRepeal(@RequestBody RepealRequest request,
                               HttpServletRequest httpServletRequest) {
        Response response = new Response();
        try {
            String token=httpServletRequest.getHeader("token");
            Map in=new HashMap();
            in.put("token", token);
            in.put("jobId", request.getJobId());
            Page<Repeal> repeals=iRepealBusinessService.listRepeal(in);
            response.setData(repeals);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
                return response;
            } catch (Exception ex2) {
                response.setErrorCode(10080);
                return response;
            }
        }
        return response;
    }

    /**
     * 设置对方用户阅读撤诉申请的时间
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/setReadTime")
    public Response setRepealReadTime(@RequestBody RepealRequest request,
                                      HttpServletRequest httpServletRequest) {
        Response response = new Response();
        try {
            String token=httpServletRequest.getHeader("token");
            Map in=new HashMap();
            in.put("token", token);
            in.put("jobId", request.getJobId());
            iRepealBusinessService.setRepealReadTime(in);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
                return response;
            } catch (Exception ex2) {
                response.setErrorCode(10081);
                return response;
            }
        }
        return response;
    }

    /**
     * 拒绝撤诉申请
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/reject")
    public Response rejectRepeal(@RequestBody RepealRequest request,
                                 HttpServletRequest httpServletRequest) {
        Response response = new Response();
        try {
            String token=httpServletRequest.getHeader("token");
            Map in=new HashMap();
            in.put("token", token);
            in.put("jobId", request.getJobId());
            in.put("processRemark", request.getProcessRemark());
            iRepealBusinessService.rejectRepeal(in);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
                return response;
            } catch (Exception ex2) {
                response.setErrorCode(10082);
                return response;
            }
        }
        return response;
    }

    /**
     * 同意撤诉申请
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/accept")
    public Response acceptRepeal(@RequestBody RepealRequest request,
                                 HttpServletRequest httpServletRequest) {
        Response response = new Response();
        try {
            String token=httpServletRequest.getHeader("token");
            Map in=new HashMap();
            in.put("token", token);
            in.put("jobId", request.getJobId());
            in.put("processRemark", request.getProcessRemark());
            iRepealBusinessService.acceptRepeal(in);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
                return response;
            } catch (Exception ex2) {
                response.setErrorCode(10083);
                return response;
            }
        }
        return response;
    }
}

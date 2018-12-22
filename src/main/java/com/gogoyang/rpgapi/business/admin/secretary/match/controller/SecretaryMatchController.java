package com.gogoyang.rpgapi.business.admin.secretary.match.controller;

import com.gogoyang.rpgapi.business.admin.secretary.match.service.ISecretaryMatchBusinessService;
import com.gogoyang.rpgapi.business.admin.vo.AdminRequest;
import com.gogoyang.rpgapi.business.vo.Response;
import com.gogoyang.rpgapi.meta.admin.entity.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/secretary")
public class SecretaryMatchController {
    private final ISecretaryMatchBusinessService iSecretaryMatchBusinessService;

    @Autowired
    public SecretaryMatchController(ISecretaryMatchBusinessService iSecretaryMatchBusinessService) {
        this.iSecretaryMatchBusinessService = iSecretaryMatchBusinessService;
    }

    /**
     * 查询等待匹配的任务，PENDING/MATCHING
     * 按createdTime排倒序
     *
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/listJobToMatch")
    public Response listJobToMatch(HttpServletRequest httpServletRequest) {
        Response response = new Response();
        try {
            String token = httpServletRequest.getHeader("token");
            Map in=new HashMap();
            in.put("token", token);
            in.put("pageIndex", 0);
            in.put("pageSize", 100);
            Map out=iSecretaryMatchBusinessService.listJobToMatch(in);
            response.setData(out);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
                return response;
            } catch (Exception ex2) {
                response.setErrorCode(10035);
                return response;
            }
        }

        return response;
    }

    /**
     * 读取申请了任务的用户和任务
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/listUserAppliedJob")
    public Response listUserAppliedJob(@RequestBody AdminRequest request,
                                       HttpServletRequest httpServletRequest) {
        Response response = new Response();
        try{
            String token=httpServletRequest.getHeader("token");
            Map in=new HashMap();
            in.put("token", token);
            in.put("pageIndex",request.getPageIndex());
            in.put("pageSize", request.getPageSize());
            Map out=iSecretaryMatchBusinessService.listUserAppliedJob(in);
            response.setData(out);
        }catch (Exception ex){
            try{
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            }catch (Exception ex2){
                response.setErrorCode(10107);
            }
        }
        return response;
    }

    /**
     * 分配一个任务给一个用户
     * Assign jobId to userId
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/matchJobToUser")
    public Response matchJobToUser(@RequestBody AdminRequest request,
                                   HttpServletRequest httpServletRequest) {
        /**
         * check whether the admin is rpg secretary
         * create a new jobMatch
         */
        Response response = new Response();
        try {
            Integer userId=request.getUserId();
            Integer jobId=request.getJobId();
            String token = httpServletRequest.getHeader("token");
            Map in=new HashMap();
            in.put("userId", userId);
            in.put("jobId", jobId);
            in.put("token", token);
            iSecretaryMatchBusinessService.matchJobToUser(in);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
                return response;
            } catch (Exception ex2) {
                response.setErrorCode(10011);
                return response;
            }
        }
        return response;
    }

    /**
     * 读取所有申请了jobId任务，且等待处理的用户
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/listUserApplyJob")
    public Response listUserApplyJob(@RequestBody AdminRequest request,
                                                  HttpServletRequest httpServletRequest) {
        Response response = new Response();
        try {
            String token = httpServletRequest.getHeader("token");
            Integer jobId=(Integer)request.getJobId();
            Map in=new HashMap();
            in.put("token", token);
            in.put("jobId", jobId);
            Map out= iSecretaryMatchBusinessService.loadUserApplyJob(in);
            response.setData(out);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
                return response;
            } catch (Exception ex2) {
                response.setErrorCode(10029);
                return response;
            }
        }
        return response;
    }
}

package com.gogoyang.rpgapi.business.admin.secretary.match.controller;

import com.gogoyang.rpgapi.business.admin.secretary.match.service.ISecretaryMatchBusinessService;
import com.gogoyang.rpgapi.business.admin.vo.AdminRequest;
import com.gogoyang.rpgapi.framework.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/secretary/match")
public class SecretaryMatchController {
    private final ISecretaryMatchBusinessService iSecretaryMatchBusinessService;

    @Autowired
    public SecretaryMatchController(ISecretaryMatchBusinessService iSecretaryMatchBusinessService) {
        this.iSecretaryMatchBusinessService = iSecretaryMatchBusinessService;
    }

    /**
     * Read new apply jobs by users
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/listAppliedJob")
    public Response listAppliedJob(@RequestBody AdminRequest request,
                                 HttpServletRequest httpServletRequest){
        Response response=new Response();
        try {
            String token=httpServletRequest.getHeader("token");
            Map in=new HashMap();
            in.put("token", token);
            in.put("pageIndex", request.getPageIndex());
            in.put("pageSize", request.getPageSize());
            Map out=iSecretaryMatchBusinessService.listAppliedJob(in);
            response.setData(out);
        }catch (Exception ex){
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            }catch (Exception ex2){
                response.setErrorCode(10109);
            }
        }
        return response;
    }

    /**
     * 读取申请任务的用户列表
     * read apply user of jobId
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/listApplyByJobId")
    public Response listApplyByJobId(@RequestBody AdminRequest request,
                                     HttpServletRequest httpServletRequest){
        Response response=new Response();
        try {
            String token=httpServletRequest.getHeader("token");
            Map in=new HashMap();
            in.put("token", token);
            in.put("pageIndex", request.getPageIndex());
            in.put("pageSize", request.getPageSize());
            in.put("jobId", request.getJobId());
            Map out=iSecretaryMatchBusinessService.listApplyByJobId(in);
            response.setData(out);
        }catch (Exception ex){
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            }catch (Exception ex2){
                response.setErrorCode(10029);
            }
        }
        return response;
    }

    @ResponseBody
    @PostMapping("/getApplyJobTiny")
    public Response getApplyJobTiny(@RequestBody AdminRequest request,
                                      HttpServletRequest httpServletRequest){
        Response response=new Response();
        try {
            String token=httpServletRequest.getHeader("token");
            Map in=new HashMap();
            in.put("token", token);
            in.put("jobId",request.getJobId());
            Map out=iSecretaryMatchBusinessService.getApplyJobTiny(in);
            response.setData(out);
        }catch (Exception ex){
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            }catch (Exception ex2){
                response.setErrorCode(10122);
            }
        }
        return response;
    }

    @ResponseBody
    @PostMapping("/getApplyJobDetail")
    public Response getApplyJobDetail(@RequestBody AdminRequest request,
                                      HttpServletRequest httpServletRequest){
        Response response=new Response();
        try {
            String token=httpServletRequest.getHeader("token");
            Map in=new HashMap();
            in.put("token", token);
            in.put("jobId",request.getJobId());
            Map out=iSecretaryMatchBusinessService.getApplyJobDetail(in);
            response.setData(out);
        }catch (Exception ex){
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            }catch (Exception ex2){
                response.setErrorCode(10122);
            }
        }
        return response;
    }

    /**
     * reject common apply
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/rejectApply")
    public Response rejectApply(@RequestBody AdminRequest request,
                                HttpServletRequest httpServletRequest){
        Response response=new Response();
        try {
            String token=httpServletRequest.getHeader("token");
            Map in=new HashMap();
            in.put("token", token);
            in.put("applyId", request.getApplyId());
            in.put("remark", request.getRemark());
            iSecretaryMatchBusinessService.rejectApply(in);
        }catch (Exception ex){
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            }catch (Exception ex2){
                response.setErrorCode(10110);
            }
        }
        return response;
    }

    /**
     * Agree common apply
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/agreeApply")
    public Response agreeApply(@RequestBody AdminRequest request,
                                HttpServletRequest httpServletRequest){
        Response response=new Response();
        try {
            String token=httpServletRequest.getHeader("token");
            Map in=new HashMap();
            in.put("token", token);
            in.put("applyId", request.getApplyId());
            iSecretaryMatchBusinessService.agreeApply(in);
        }catch (Exception ex){
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            }catch (Exception ex2){
                response.setErrorCode(10110);
            }
        }
        return response;
    }

    /**
     * read history of user apply
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/listApplyHistory")
    public Response listApplyHistory(@RequestBody AdminRequest request,
                                     HttpServletRequest httpServletRequest){
        Response response=new Response();
        try{
            String token=httpServletRequest.getHeader("token");
            Map in=new HashMap();
            in.put("token", token);
            in.put("userId", request.getUserId());
            in.put("pageIndex", request.getPageIndex());
            in.put("pageSize", request.getPageSize());
            Map out=iSecretaryMatchBusinessService.listApplyHistory(in);
            response.setData(out);
        }catch (Exception ex){
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            }catch (Exception ex2){
                response.setErrorCode(10108);
            }
        }
        return response;
    }

    @ResponseBody
    @PostMapping("/getApplyDetail")
    public Response getApplyDetail(@RequestBody AdminRequest request,
                                   HttpServletRequest httpServletRequest){
        Response response=new Response();
        try {
            String token=httpServletRequest.getHeader("token");
            Map in=new HashMap();
            in.put("token", token);
            in.put("applyId", request.getApplyId());
            Map out=iSecretaryMatchBusinessService.getApplyDetail(in);
            response.setData(out);
        }catch (Exception ex        ){
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            }catch (Exception ex2){
                response.setErrorCode(10123);
            }
        }
        return response;
    }
}

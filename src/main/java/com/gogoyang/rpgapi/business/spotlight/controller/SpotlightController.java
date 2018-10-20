package com.gogoyang.rpgapi.business.spotlight.controller;

import com.gogoyang.rpgapi.business.spotlight.vo.SpotlightRequest;
import com.gogoyang.rpgapi.business.vo.Response;
import com.gogoyang.rpgapi.meta.spotlight.entity.Spot;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("spotlight")
public class SpotlightController {
    /**
     * 本spotlight用于申诉广场的信息显示
     * 以及，甲方或乙方查看任务的申诉详情
     */

    /**
     * 创建一个申诉事件
     * Create a spotlight
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/create")
    public Response createSpot(@RequestBody SpotlightRequest request,
                           HttpServletRequest httpServletRequest){
        Response response=new Response();
        try {
            String token=httpServletRequest.getHeader("token");
            Map in=new HashMap();
            in.put("token", token);
            in.put("content", request.getContent());
            in.put("jobId", request.getJobId());
            in.put("title", request.getTitle());



        }catch (Exception ex){
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
                return response;
            }catch (Exception ex2){
                response.setErrorCode(10076);
                return response;
            }
        }
        return response;
    }

    /**
     * 申诉广场读取当前的申诉事件
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/list")
    public Response listSpotlight(@RequestBody SpotlightRequest request,
                                  HttpServletRequest httpServletRequest){
        Response response=new Response();
        try {

        }catch (Exception ex){
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
                return response;
            }catch (Exception ex2){
                response.setErrorCode(10076);
                return response;
            }
        }
        return response;
    }

    /**
     * 读取一个简要的申诉事件
     * 不包含详细信息
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/tiny")
    public Response getSpotlightTiny(@RequestBody SpotlightRequest request,
                                  HttpServletRequest httpServletRequest){
        Response response=new Response();
        try {

        }catch (Exception ex){
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
                return response;
            }catch (Exception ex2){
                response.setErrorCode(10076);
                return response;
            }
        }
        return response;
    }

    /**
     * 读取一个详细的申诉事件
     * 包含申诉详情
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/detail")
    public Response getSpotlightDetail(@RequestBody SpotlightRequest request,
                                     HttpServletRequest httpServletRequest){
        Response response=new Response();
        try {

        }catch (Exception ex){
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
                return response;
            }catch (Exception ex2){
                response.setErrorCode(10076);
                return response;
            }
        }
        return response;
    }


}

package com.gogoyang.rpgapi.business.spotlight.controller;

import com.gogoyang.rpgapi.business.spotlight.service.ISpotlightBusinessService;
import com.gogoyang.rpgapi.business.spotlight.vo.SpotlightRequest;
import com.gogoyang.rpgapi.framework.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/rpgapi/spotlight")
public class SpotlightController {
    private final ISpotlightBusinessService iSpotlightBusinessService;

    @Autowired
    public SpotlightController(ISpotlightBusinessService iSpotlightBusinessService) {
        this.iSpotlightBusinessService = iSpotlightBusinessService;
    }
    /**
     * 本spotlight用于申诉广场的信息显示
     * 以及，甲方或乙方查看任务的申诉详情
     */

    /**
     * 申诉广场读取当前的申诉事件
     */
    @ResponseBody
    @PostMapping("/listSpotlight")
    public Response listSpotlight(@RequestBody SpotlightRequest request){
        Response response=new Response();
        try {
            Map in=new HashMap();
            in.put("pageIndex", request.getPageIndex());
            in.put("pageSize", request.getPageSize());
            Map out=iSpotlightBusinessService.listSpotlight(in);
            response.setData(out);
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
     */
    @ResponseBody
    @GetMapping("/detail/{spotId}")
    public Response getSpotlightDetail(@PathVariable Integer spotId){
        Response response=new Response();
        try {
            Map in=new HashMap();
            in.put("spotId", spotId);
            Map out=iSpotlightBusinessService.getSpotlightDetail(in);
            response.setData(out);
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
     * create a spot book
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/createSpotBook")
    public Response createSpotBook(@RequestBody SpotlightRequest request,
                                   HttpServletRequest httpServletRequest){
        Response response=new Response();
        try {
            String token=httpServletRequest.getHeader("token");
            Map in=new HashMap();
            in.put("token", token);
            in.put("spotId", request.getSpotId());
            in.put("content", request.getContent());
            iSpotlightBusinessService.createSpotBook(in);
        }catch (Exception ex){
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            }catch (Exception ex2){
                response.setErrorCode(10113);
            }
        }
        return response;
    }


}

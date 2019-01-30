package com.gogoyang.rpgapi.business.honor.controller;

import com.gogoyang.rpgapi.business.honor.service.IHonorBusinessService;
import com.gogoyang.rpgapi.business.honor.vo.HonorRequest;
import com.gogoyang.rpgapi.business.vo.Response;
import com.gogoyang.rpgapi.meta.honor.entity.Honor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/honor")
public class HonorController {
    private final IHonorBusinessService iHonorBusinessService;

    @Autowired
    public HonorController(IHonorBusinessService iHonorBusinessService) {
        this.iHonorBusinessService = iHonorBusinessService;
    }

    @ResponseBody
    @PostMapping("/listMyHonor")
    public Response listMyHonor(@RequestBody HonorRequest request,
                                HttpServletRequest httpServletRequest){
        Response response=new Response();
        try {
            String token=httpServletRequest.getHeader("token");
            Map in=new HashMap();
            in.put("token", token);
            in.put("pageIndex", request.getPageIndex());
            in.put("pageSize", request.getPageSize());
            Map out=iHonorBusinessService.listMyHonor(in);
            response.setData(out);
        }catch (Exception ex){
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            }catch (Exception ex2){
                response.setErrorCode(10134);
            }
        }
        return response;
    }

    @ResponseBody
    @PostMapping("/loadUserHonorBalance")
    public Response loadMyHonorBalance(@RequestBody HonorRequest request,
                                       HttpServletRequest httpServletRequest){
        Response response=new Response();
        try {
            String token=httpServletRequest.getHeader("token");
            Map in=new HashMap();
            in.put("token", token);
            Map out=iHonorBusinessService.loadUserHonorBalance(in);
            response.setData(out);
        }catch (Exception ex){
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            }catch (Exception ex2){
                response.setErrorCode(10134);
            }
        }
        return response;
    }
}

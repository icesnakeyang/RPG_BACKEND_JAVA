package com.gogoyang.rpgapi.business.job.myMatch.controller;

import com.gogoyang.rpgapi.business.job.myMatch.service.IMyMatchBusinessService;
import com.gogoyang.rpgapi.business.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/job/myMatch")
public class MyMatchController {
    private final IMyMatchBusinessService iMyMatchBusinessService;

    @Autowired
    public MyMatchController(IMyMatchBusinessService iMyMatchBusinessService) {
        this.iMyMatchBusinessService = iMyMatchBusinessService;
    }

    /**
     * 用户读取RPG秘书分配给自己的任务
     *
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @GetMapping("/loadJobMatchToMe")
    public Response loadJobMatchToMe(HttpServletRequest httpServletRequest) {
        Response response = new Response();
        try {
            String token=httpServletRequest.getHeader("token");
            Map in =new HashMap();
            in.put("token", token);

            Map out=iMyMatchBusinessService.loadJobMatchToMe(in);
            response.setData(out);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            } catch (Exception ex2) {
                response.setErrorCode(10013);
            }
            return response;
        }
        return response;
    }
}

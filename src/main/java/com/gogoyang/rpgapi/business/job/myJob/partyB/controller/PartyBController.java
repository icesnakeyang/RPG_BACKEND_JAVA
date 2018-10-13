package com.gogoyang.rpgapi.business.job.myJob.partyB.controller;

import com.gogoyang.rpgapi.business.job.myJob.partyB.service.IPartyBBusinessService;
import com.gogoyang.rpgapi.business.job.vo.JobRequest;
import com.gogoyang.rpgapi.business.vo.Response;
import com.gogoyang.rpgapi.meta.job.entity.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/job/partyB")
public class PartyBController {
    private final IPartyBBusinessService iPartyBBusinessService;

    @Autowired
    public PartyBController(IPartyBBusinessService iPartyBBusinessService) {
        this.iPartyBBusinessService = iPartyBBusinessService;
    }

    @ResponseBody
    @PostMapping("/loadMyPartyBJob")
    public Response loadMyPartyBJob(@RequestBody JobRequest request,
                                    HttpServletRequest httpServletRequest){
    Response response =new Response();
    try {
        String token=httpServletRequest.getHeader("token");
        Map in =new HashMap();
        in.put("token", token);
        Page<Job> jobPage=iPartyBBusinessService.loadMyPartyBJob(in);
        response.setData(jobPage);
    }catch (Exception ex){
        try {
            response.setErrorCode(Integer.parseInt(ex.getMessage()));
            return response;
        }catch (Exception ex2){
            response.setErrorCode(10051);
        }
    }
    return  response;
    }
}
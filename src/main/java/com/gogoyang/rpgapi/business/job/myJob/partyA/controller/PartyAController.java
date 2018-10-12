package com.gogoyang.rpgapi.business.job.myJob.partyA.controller;

import com.gogoyang.rpgapi.business.job.myJob.partyA.service.IPartyABusinessService;
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
@RequestMapping("/job/partyA")
public class PartyAController {
    private final IPartyABusinessService iPartyABusinessService;

    @Autowired
    public PartyAController(IPartyABusinessService iPartyABusinessService) {
        this.iPartyABusinessService = iPartyABusinessService;
    }

    @ResponseBody
    @PostMapping("loadMyPartyAJob")
    public Response loadMyPartyAJob(@RequestBody JobRequest request,
                                    HttpServletRequest httpServletRequest){
        Response response=new Response();
        try {
            String token=httpServletRequest.getHeader("token");
            Map in =new HashMap();
            in.put("token", token);

            Page<Job> jobPage=iPartyABusinessService.loadMyPartyAJob(in);
            response.setData(jobPage);
        }catch (Exception ex){
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
                return response;
            }catch (Exception ex2){
                response.setErrorCode(10051);
                return response;
            }
        }

        return response;
    }
}

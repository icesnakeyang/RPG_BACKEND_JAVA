package com.gogoyang.rpgapi.business.job.myJob.partyA.controller;

import com.gogoyang.rpgapi.business.job.myJob.partyA.service.IPartyABusinessService;
import com.gogoyang.rpgapi.business.job.vo.JobRequest;
import com.gogoyang.rpgapi.framework.vo.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/rpgapi/job/partyA")
public class PartyAController {
    private final IPartyABusinessService iPartyABusinessService;

    private Logger logger= LoggerFactory.getLogger(getClass());

    @Autowired
    public PartyAController(IPartyABusinessService iPartyABusinessService) {
        this.iPartyABusinessService = iPartyABusinessService;
    }

    @ResponseBody
    @PostMapping("listMyPartyAJob")
    public Response listMyPartyAJob(@RequestBody JobRequest request,
                                    HttpServletRequest httpServletRequest){
        Response response=new Response();
        try {
            String token=httpServletRequest.getHeader("token");
            Map in =new HashMap();
            in.put("token", token);
            in.put("pageIndex", request.getPageIndex());
            in.put("pageSize", request.getPageSize());
            in.put("statusList", request.getStatusList());
            in.put("searchKey", request.getSearchKey());

            Map out=iPartyABusinessService.listMyPartyAJob(in);
            response.setData(out);
        }catch (Exception ex){
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            }catch (Exception ex2){
                response.setErrorCode(30000);
                logger.error(ex.getMessage());
            }
        }

        return response;
    }

    /**
     * 甲方读取任务详情
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("getPartyAJob")
    public Response getPartyAJob(@RequestBody JobRequest request,
                                    HttpServletRequest httpServletRequest){
        Response response=new Response();
        try {
            String token=httpServletRequest.getHeader("token");
            Map in =new HashMap();
            in.put("token", token);
            in.put("jobId", request.getJobId());

            Map out=iPartyABusinessService.getPartyAJob(in);
            response.setData(out);
        }catch (Exception ex){
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            }catch (Exception ex2){
                response.setErrorCode(30000);
                logger.error(ex.getMessage());
            }
        }

        return response;
    }
}

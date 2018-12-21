package com.gogoyang.rpgapi.business.jobPlaza.controller;

import com.gogoyang.rpgapi.business.job.vo.JobRequest;
import com.gogoyang.rpgapi.business.jobPlaza.service.IJobPlazaBusinessService;
import com.gogoyang.rpgapi.business.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/job/plaza")
public class JobPlazaController {
    private final IJobPlazaBusinessService iJobPlazaBusinessService;

    @Autowired
    public JobPlazaController(IJobPlazaBusinessService iJobPlazaBusinessService) {
        this.iJobPlazaBusinessService = iJobPlazaBusinessService;
    }

    /**
     * 在任务广场显示一批目前正在等待交易的任务
     * read unassigned jobs to display in job plaza
     *
     * @return
     */
    @ResponseBody
    @PostMapping("/publicJob")
    public Response publicJob(@RequestBody JobRequest request) {
        Response response = new Response();
        try {
            Map in=new HashMap();
            in.put("pageIndex", request.getPageIndex());
            in.put("pageSize", request.getPageSize());
            Map out =iJobPlazaBusinessService.loadPublicJob(in);
            response.setData(out);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
                return response;
            } catch (Exception ex2) {
                response.setErrorCode(10026);
                System.out.println(ex.getMessage());
                return response;
            }
        }
        return response;
    }
}

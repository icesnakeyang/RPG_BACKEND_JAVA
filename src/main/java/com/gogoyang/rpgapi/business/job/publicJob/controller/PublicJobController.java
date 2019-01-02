package com.gogoyang.rpgapi.business.job.publicJob.controller;

import com.gogoyang.rpgapi.business.job.publicJob.service.IPublicJobBusinessService;
import com.gogoyang.rpgapi.business.job.publicJob.vo.PublicJobRequest;
import com.gogoyang.rpgapi.business.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/public_job")
public class PublicJobController {
    private final IPublicJobBusinessService iPublicJobBusinessService;

    @Autowired
    public PublicJobController(IPublicJobBusinessService iPublicJobBusinessService) {
        this.iPublicJobBusinessService = iPublicJobBusinessService;
    }

    @ResponseBody
    @PostMapping("/listPublicJob")
    public Response listPublicJob(@RequestBody PublicJobRequest request){
        Response response=new Response();
        try {
            Map in=new HashMap();
            in.put("pageIndex", request.getPageIndex());
            in.put("pageSize", request.getPageSize());
            Map out=iPublicJobBusinessService.listPublicJob(in);
            response.setData(out);
        }catch (Exception ex){
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            }catch (Exception ex2){
                response.setErrorCode(10026);
            }
        }
        return response;
    }

    /**
     * 读取一条任务，包含详情
     * read a common with jobDetail
     *
     * @param jobId
     * @return
     */
    @ResponseBody
    @GetMapping("/{jobId}")
    public Response loadJobDetail(@PathVariable Integer jobId) {
        Response response = new Response();
        try {
            Map in=new HashMap();
            in.put("jobId", jobId);
            Map out=iPublicJobBusinessService.getJobDetail(in);
            response.setData(out);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
                return response;
            } catch (Exception ex2) {
                response.setErrorCode(10026);
                return response;
            }
        }

        return response;
    }
}

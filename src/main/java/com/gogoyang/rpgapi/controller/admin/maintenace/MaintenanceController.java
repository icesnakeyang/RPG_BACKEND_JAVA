package com.gogoyang.rpgapi.controller.admin.maintenace;

import com.gogoyang.rpgapi.business.admin.secretary.maintenance.IMaintenanceBService;
import com.gogoyang.rpgapi.framework.vo.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/rpgapi/admin/maintenance")
public class MaintenanceController {
    private final IMaintenanceBService iMaintenanceBService;

    public MaintenanceController(IMaintenanceBService iMaintenanceBService) {
        this.iMaintenanceBService = iMaintenanceBService;
    }

    /**
     * 检查未成交任务时间，把超过期限的任务设置为过期
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/overdueJobs")
    public Response overdueJobs(@RequestBody MaintenanceRequest request,
                                HttpServletRequest httpServletRequest) {
        Response response = new Response();
        Map in = new HashMap();
        try {
            String token = httpServletRequest.getHeader("token");
            in.put("token", token);

            iMaintenanceBService.overdueJobs(in);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            } catch (Exception ex2) {
                response.setErrorCode(20002);
                log.error("overdueJobs error: " + ex.getMessage());
            }
        }
        return response;
    }
}

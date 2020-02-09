package com.gogoyang.rpgapi.controller.admin.userActionLog;

import com.alibaba.fastjson.JSON;
import com.gogoyang.rpgapi.business.admin.userActionLog.IAdminUserActionLogBusinessService;
import com.gogoyang.rpgapi.framework.vo.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/userActionLog")
public class UserActionLogController {
    private final IAdminUserActionLogBusinessService iAdminUserActionLogBusinessService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    public UserActionLogController(IAdminUserActionLogBusinessService iAdminUserActionLogBusinessService) {
        this.iAdminUserActionLogBusinessService = iAdminUserActionLogBusinessService;
    }

    @ResponseBody
    @PostMapping("/listUserActionLog")
    public Response listUserActionLog(@RequestBody UserActionLogRequest request,
                                      HttpServletRequest httpServletRequest) {
        Response response = new Response();
        Map in = new HashMap();
        try {
            String token = httpServletRequest.getHeader("token");
            in.put("token", token);
            in.put("pageIndex", request.getPageIndex());
            in.put("pageSize", request.getPageSize());
            Map out = iAdminUserActionLogBusinessService.listUserActionLog(in);
            response.setData(out);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            } catch (Exception ex2) {
                response.setErrorCode(20002);
                logger.error("Controller: listUserActionLog: " + ex.getMessage() + "-- request: " + JSON.toJSONString(request));
            }
        }
        return response;
    }
}

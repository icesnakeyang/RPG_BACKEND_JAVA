package com.gogoyang.rpgapi.controller.admin.userActionLog;

import com.alibaba.fastjson.JSON;
import com.gogoyang.rpgapi.business.admin.userActionLog.IAdminUserActionLogBusinessService;
import com.gogoyang.rpgapi.framework.vo.Response;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/rpgapi/admin/userActionLog")
public class UserActionLogController {
    private final IAdminUserActionLogBusinessService iAdminUserActionLogBusinessService;

    public UserActionLogController(IAdminUserActionLogBusinessService iAdminUserActionLogBusinessService) {
        this.iAdminUserActionLogBusinessService = iAdminUserActionLogBusinessService;
    }

    /**
     * 查询用户行为记录
     * @param request
     * @param httpServletRequest
     * @return
     */
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
                log.error("Controller: listUserActionLog: " + ex.getMessage() + "-- request: " + JSON.toJSONString(request));
            }
        }
        return response;
    }
}

package com.gogoyang.rpgapi.controller.admin.userActionLog;

import com.gogoyang.rpgapi.controller.vo.Response;
import com.gogoyang.rpgapi.meta.userAction.service.IUserActionLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/userActionLog")
public class UserActionLogController {
    private IUserActionLogService iUserActionLogService;
    private Logger logger= LoggerFactory.getLogger(getClass());
    public Response listUserActionLog(@RequestBody UserActionLogRequest request,
                                      HttpServletRequest httpServletRequest){
        Response response=new Response();
        Map in=new HashMap();
        try {
            String token=httpServletRequest.getHeader("token");
            in.put("token", token);
            in.put("pageIndex", request.getPageIndex());
            in.put("pageSize", request.getPageSize());
            Map out=
        }
    }
}

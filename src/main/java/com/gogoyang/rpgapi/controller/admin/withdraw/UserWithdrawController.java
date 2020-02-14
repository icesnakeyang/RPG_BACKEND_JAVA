package com.gogoyang.rpgapi.controller.admin.withdraw;

import com.gogoyang.rpgapi.business.admin.withdraw.IAdminWithdrawBusinessService;
import com.gogoyang.rpgapi.framework.vo.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("rpgapi/admin/userWithdraw")
public class UserWithdrawController {
    private final IAdminWithdrawBusinessService iAdminWithdrawBusinessService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    public UserWithdrawController(IAdminWithdrawBusinessService iAdminWithdrawBusinessService) {
        this.iAdminWithdrawBusinessService = iAdminWithdrawBusinessService;
    }

    @ResponseBody
    @PostMapping("/listUserWithdrawApplys")
    public Response listUserWithdrawApplys(@RequestBody UserWithdrawRequest request,
                                           HttpServletRequest httpServletRequest) {
        Response response = new Response();
        Map in = new HashMap();
        try {
            String token = httpServletRequest.getHeader("token");
            in.put("token", token);
            in.put("pageIndex", request.getPageIndex());
            in.put("pageSize", request.getPageSize());

            Map out = iAdminWithdrawBusinessService.listUserWithdrawApplys(in);
            response.setData(out);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            } catch (Exception ex2) {
                response.setErrorCode(20006);
            }
        }
        return response;
    }
}

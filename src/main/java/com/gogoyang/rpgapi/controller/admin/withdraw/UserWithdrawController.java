package com.gogoyang.rpgapi.controller.admin.withdraw;

import com.gogoyang.rpgapi.business.admin.withdraw.IAdminWithdrawBusinessService;
import com.gogoyang.rpgapi.framework.vo.Response;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Slf4j
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

    @ResponseBody
    @PostMapping("/getWithdrawApplys")
    public Response getWithdrawApplys(@RequestBody UserWithdrawRequest request,
                                           HttpServletRequest httpServletRequest) {
        Response response = new Response();
        Map in = new HashMap();
        try {
            String token = httpServletRequest.getHeader("token");
            in.put("token", token);
            in.put("withdrawLedgerId", request.getWithdrawLedgerId());

            Map out = iAdminWithdrawBusinessService.getWithdrawApplys(in);
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

    /**
     * 管理员通过用户取现申请
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/agreeWithdraw")
    public Response agreeWithdraw(@RequestBody UserWithdrawRequest request,
                                           HttpServletRequest httpServletRequest) {
        Response response = new Response();
        Map in = new HashMap();
        try {
            String token = httpServletRequest.getHeader("token");
            in.put("token", token);
            in.put("withdrawLedgerId", request.getWithdrawLedgerId());

            iAdminWithdrawBusinessService.agreeWithdraw(in);
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

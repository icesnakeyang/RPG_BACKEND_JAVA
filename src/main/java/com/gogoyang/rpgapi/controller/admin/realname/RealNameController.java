package com.gogoyang.rpgapi.controller.admin.realname;

import com.gogoyang.rpgapi.business.admin.secretary.realname.ISecretaryRealnameBusinessService;
import com.gogoyang.rpgapi.framework.vo.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/rpgapi/admin/realname")
public class RealNameController {
    private final ISecretaryRealnameBusinessService iSecretaryRealnameBusinessService;

    public RealNameController(ISecretaryRealnameBusinessService iSecretaryRealnameBusinessService) {
        this.iSecretaryRealnameBusinessService = iSecretaryRealnameBusinessService;
    }

    /**
     * 秘书读取用户的实名认证申请列表
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/listRealnamePending")
    public Response listRealnamePending(@RequestBody SecretaryRealnameRequest request,
                                        HttpServletRequest httpServletRequest) {
        Response response = new Response();
        Map in = new HashMap();
        try {
            String token = httpServletRequest.getHeader("token");
            in.put("token", token);
            in.put("pageIndex", request.getPageIndex());
            in.put("pageSize", request.getPageSize());

            Map out = iSecretaryRealnameBusinessService.listRealnamePending(in);
            response.setData(out);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            } catch (Exception ex2) {
                response.setErrorCode(30000);
                log.error("listRealnamePending error:" + ex.getMessage());
            }
        }
        return response;
    }

    /**
     * 秘书读取用户的实名认证申请详情
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/getRealnamePending")
    public Response getRealnamePending(@RequestBody SecretaryRealnameRequest request,
                                        HttpServletRequest httpServletRequest) {
        Response response = new Response();
        Map in = new HashMap();
        try {
            String token = httpServletRequest.getHeader("token");
            in.put("token", token);
            in.put("userId", request.getUserId());

            Map out = iSecretaryRealnameBusinessService.getRealnamePending(in);
            response.setData(out);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            } catch (Exception ex2) {
                response.setErrorCode(30000);
                log.error("getRealnamePending error:" + ex.getMessage());
            }
        }
        return response;
    }

    /**
     * 秘书拒绝用户的实名认证申请详情
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/rejectRealname")
    public Response rejectRealname(@RequestBody SecretaryRealnameRequest request,
                                        HttpServletRequest httpServletRequest) {
        Response response = new Response();
        Map in = new HashMap();
        try {
            String token = httpServletRequest.getHeader("token");
            in.put("token", token);
            in.put("userId", request.getUserId());
            in.put("remark",request.getRemark());

            iSecretaryRealnameBusinessService.rejectRealname(in);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            } catch (Exception ex2) {
                response.setErrorCode(30000);
                log.error("rejectRealname error:" + ex.getMessage());
            }
        }
        return response;
    }

    /**
     * 秘书通过用户的实名认证申请详情
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/agreeRealname")
    public Response agreeRealname(@RequestBody SecretaryRealnameRequest request,
                                        HttpServletRequest httpServletRequest) {
        Response response = new Response();
        Map in = new HashMap();
        try {
            String token = httpServletRequest.getHeader("token");
            in.put("token", token);
            in.put("userId", request.getUserId());

            iSecretaryRealnameBusinessService.agreeRealname(in);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            } catch (Exception ex2) {
                response.setErrorCode(30000);
                log.error("agreeRealname error:" + ex.getMessage());
            }
        }
        return response;
    }
}

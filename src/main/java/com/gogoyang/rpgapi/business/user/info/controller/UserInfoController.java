package com.gogoyang.rpgapi.business.user.info.controller;

import com.gogoyang.rpgapi.business.user.UserRequest;
import com.gogoyang.rpgapi.business.user.info.service.IUserInfoBusinessService;
import com.gogoyang.rpgapi.business.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user/info")
public class UserInfoController {
    private final IUserInfoBusinessService iUserInfoBusinessService;

    @Autowired
    public UserInfoController(IUserInfoBusinessService iUserInfoBusinessService) {
        this.iUserInfoBusinessService = iUserInfoBusinessService;
    }

    /**
     * 保存用户的email，phone，realname
     *
     * @param request
     * @param httpServletRequest
     * @return
     */
    @ResponseBody
    @PostMapping("/saveContactInfo")
    public Response saveContactInfo(@RequestBody UserRequest request,
                                    HttpServletRequest httpServletRequest) {
        Response response = new Response();
        try {
            Map in = new HashMap();
            String token = httpServletRequest.getHeader("token");
            in.put("token", token);
            in.put("email", request.getEmail());
            in.put("phone", request.getPhone());
            in.put("realName", request.getRealName());

            iUserInfoBusinessService.saveUserContactInfo(in);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
                return response;
            } catch (Exception ex2) {
                response.setErrorCode(10048);
            }
        }
        return response;
    }
}

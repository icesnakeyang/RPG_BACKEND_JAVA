package com.gogoyang.rpgapi.business.user.register.controller;

import com.gogoyang.rpgapi.business.user.register.service.IUserRegisterBusinessService;
import com.gogoyang.rpgapi.business.user.register.vo.RegisterRequest;
import com.gogoyang.rpgapi.business.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user/register")
public class UserRegisterController {
    private final IUserRegisterBusinessService iUserRegisterBusinessService;

    @Autowired
    public UserRegisterController(IUserRegisterBusinessService iUserRegisterBusinessService) {
        this.iUserRegisterBusinessService = iUserRegisterBusinessService;
    }

    /**
     * register account with email
     * @param request
     * @return
     */
    @ResponseBody
    @PostMapping("/registerByEmail")
    public Response registerByEmail(@RequestBody RegisterRequest request) {
        Response response = new Response();
        try {
            Map in=new HashMap();
            in.put("email", request.getEmail());
            in.put("loginPassword", request.getLoginPassword());
            Map out=iUserRegisterBusinessService.registerByEmail(in);
            response.setData(out);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            } catch (Exception ex2) {
                response.setErrorCode(10014);
            }
        }
        return response;
    }
}

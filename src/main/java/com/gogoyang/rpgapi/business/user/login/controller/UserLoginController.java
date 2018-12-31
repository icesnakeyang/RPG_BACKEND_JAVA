package com.gogoyang.rpgapi.business.user.login.controller;

import com.gogoyang.rpgapi.business.user.login.service.IUserLoginBusinessService;
import com.gogoyang.rpgapi.business.user.login.vo.LoginRequest;
import com.gogoyang.rpgapi.business.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user/login")
public class UserLoginController {
    private final IUserLoginBusinessService iUserLoginBusinessService;

    @Autowired
    public UserLoginController(IUserLoginBusinessService iUserLoginBusinessService) {
        this.iUserLoginBusinessService = iUserLoginBusinessService;
    }

    @ResponseBody
    @PostMapping("/loginByEmail")
    public Response login(@RequestBody LoginRequest request){
        Response response=new Response();
        try {
            Map in=new HashMap();
            in.put("email", request.getEmail());
            in.put("password", request.getPassword());
            Map out=iUserLoginBusinessService.loginByEmail(in);
            response.setData(out);
        }catch (Exception ex){
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            }catch (Exception ex2){
                response.setErrorCode(10024);
            }
        }
        return response;
    }
}

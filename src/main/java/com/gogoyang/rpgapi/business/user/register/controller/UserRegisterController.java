package com.gogoyang.rpgapi.business.user.register.controller;

import com.gogoyang.rpgapi.business.user.register.service.IUserRegisterBusinessService;
import com.gogoyang.rpgapi.business.user.register.vo.RegisterRequest;
import com.gogoyang.rpgapi.business.vo.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user/register")
public class UserRegisterController {
    private final IUserRegisterBusinessService iUserRegisterBusinessService;

    private Logger logger= LoggerFactory.getLogger(getClass());

    @Autowired
    public UserRegisterController(IUserRegisterBusinessService iUserRegisterBusinessService) {
        this.iUserRegisterBusinessService = iUserRegisterBusinessService;
    }

    /**
     * register account with email
     *
     * @param request
     * @return
     */
    @ResponseBody
    @PostMapping("/registerByEmail")
    public Response registerByEmail(@RequestBody RegisterRequest request) {
        Response response = new Response();
        try {
            Map in = new HashMap();
            in.put("email", request.getEmail());
            in.put("loginPassword", request.getLoginPassword());
            in.put("realName", request.getRealName());
            Map out = iUserRegisterBusinessService.registerByEmail(in);
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

    /**
     * Read email from database
     *
     * @param request
     * @return
     */
    @ResponseBody
    @PostMapping("/getEmailByEmail")
    public Response getEmailByEmail(@RequestBody RegisterRequest request) {
        Response response = new Response();
        try {
            String emailStr = request.getEmail();
            Map in = new HashMap();
            in.put("email", emailStr);
            Map out = iUserRegisterBusinessService.getEmailByEmail(in);
            response.setData(out);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            } catch (Exception ex2) {
                response.setErrorCode(10118);
            }
        }
        return response;
    }

    @ResponseBody
    @PostMapping("/getPhone")
    public Response getPhone(@RequestBody RegisterRequest request) {
        Response response = new Response();
        Map in = new HashMap();
        Map out = new HashMap();
        try {
            in.put("phone", request.getPhone());
            out = iUserRegisterBusinessService.getPhone(in);
            response.setData(out);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            } catch (Exception ex2) {
                response.setErrorCode(10124);
            }
        }
        return response;
    }

    /**
     * register account by phone
     *
     * @param request
     * @return
     */
    @ResponseBody
    @PostMapping("/registerByPhone")
    public Response registerByPhone(@RequestBody RegisterRequest request) {
        Response response = new Response();
        try {
            Map in = new HashMap();
            in.put("phone", request.getPhone());
            in.put("code", request.getCode());
            in.put("loginPassword", request.getLoginPassword());
            in.put("realName", request.getRealName());
            Map out = iUserRegisterBusinessService.registerByPhone(in);
            response.setData(out);
        } catch (Exception ex) {
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            } catch (Exception ex2) {
                response.setErrorCode(10014);
                logger.error(ex.getMessage());
            }
        }
        return response;
    }
}

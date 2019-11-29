package com.gogoyang.rpgapi.business.user.login.controller;

import com.gogoyang.rpgapi.business.common.ICommonBusinessService;
import com.gogoyang.rpgapi.business.user.login.service.IUserLoginBusinessService;
import com.gogoyang.rpgapi.business.user.login.vo.LoginRequest;
import com.gogoyang.rpgapi.business.vo.Response;
import com.gogoyang.rpgapi.framework.constant.GogoActType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user/login")
public class UserLoginController {
    private final IUserLoginBusinessService iUserLoginBusinessService;
    private final ICommonBusinessService iCommonBusinessService;

    private Logger logger= LoggerFactory.getLogger(getClass());

    @Autowired
    public UserLoginController(IUserLoginBusinessService iUserLoginBusinessService,
                               ICommonBusinessService iCommonBusinessService) {
        this.iUserLoginBusinessService = iUserLoginBusinessService;
        this.iCommonBusinessService = iCommonBusinessService;
    }

    @ResponseBody
    @PostMapping("/login")
    public Response login(@RequestBody LoginRequest request){
        Response response=new Response();
        Map in=new HashMap();
        Map logMap=new HashMap();
        Map memoMap=new HashMap();
        try {
            in.put("email", request.getEmail());
            in.put("phone", request.getPhone());
            in.put("password", request.getPassword());
            Map out=iUserLoginBusinessService.login(in);
            response.setData(out);
            logMap.put("GogoActType", GogoActType.LOGIN);
            memoMap.put("email", request.getEmail());
            memoMap.put("phone", request.getPhone());
        }catch (Exception ex){
            try {
                response.setErrorCode(Integer.parseInt(ex.getMessage()));
            }catch (Exception ex2){
                response.setErrorCode(10024);
                logger.error(ex.getMessage());
            }
            memoMap.put("error", ex.getMessage());
        }
        try {
            logMap.put("memo", memoMap);
            iCommonBusinessService.createUserActionLog(logMap);
        }catch (Exception ex3){
            logger.error(ex3.getMessage());
        }
        return response;
    }
}

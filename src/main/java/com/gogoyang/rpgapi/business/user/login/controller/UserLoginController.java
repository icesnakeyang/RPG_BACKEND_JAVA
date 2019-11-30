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


}

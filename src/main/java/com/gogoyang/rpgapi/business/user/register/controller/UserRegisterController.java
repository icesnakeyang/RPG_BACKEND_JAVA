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


}

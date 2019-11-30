package com.gogoyang.rpgapi.business.user.login.service;

import com.gogoyang.rpgapi.framework.common.IRPGFunction;
import com.gogoyang.rpgapi.framework.constant.RoleType;
import com.gogoyang.rpgapi.meta.email.entity.Email;
import com.gogoyang.rpgapi.meta.email.service.IEmailService;
import com.gogoyang.rpgapi.meta.user.entity.User;
import com.gogoyang.rpgapi.meta.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class UserLoginBusinessService implements IUserLoginBusinessService {
    private final IEmailService iEmailService;
    private final IUserService iUserService;
    private final IRPGFunction irpgFunction;

    @Autowired
    public UserLoginBusinessService(IEmailService iEmailService,
                                    IUserService iUserService,
                                    IRPGFunction irpgFunction) {
        this.iEmailService = iEmailService;
        this.iUserService = iUserService;
        this.irpgFunction = irpgFunction;
    }



}

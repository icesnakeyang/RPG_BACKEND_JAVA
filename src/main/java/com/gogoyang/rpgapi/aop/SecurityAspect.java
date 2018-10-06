package com.gogoyang.rpgapi.aop;

import com.gogoyang.rpgapi.user.userInfo.entity.UserInfo;
import com.gogoyang.rpgapi.user.userInfo.service.IUserInfoService;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SecurityAspect {
    @Autowired
    IUserInfoService iUserInfoService;

    @Pointcut("@annotation(SignedUser)")
    public void signedUser(){

    }

    @Before("signedUser()")
    public void checkToken() throws Exception{
        String token=AspectVariable.getToken();
        UserInfo userInfo=iUserInfoService.loadUserByToken(token);
        if(userInfo==null){
            throw new Exception("10004");
        }
    }
}

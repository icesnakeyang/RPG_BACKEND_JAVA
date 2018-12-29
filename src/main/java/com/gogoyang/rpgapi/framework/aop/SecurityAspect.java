package com.gogoyang.rpgapi.framework.aop;

import com.gogoyang.rpgapi.meta.user.entity.User;
import com.gogoyang.rpgapi.meta.user.service.IUserService;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SecurityAspect {
    private final IUserService iUserService;

    @Autowired
    public SecurityAspect(IUserService iUserService) {
        this.iUserService = iUserService;
    }

    @Pointcut("@annotation(SignedUser)")
    public void signedUser(){

    }

    @Before("signedUser()")
    public void checkToken() throws Exception{
        String token=AspectVariable.getToken();
        User userInfo=iUserService.getUserByToken(token);
        if(userInfo==null){
            throw new Exception("10004");
        }
    }
}

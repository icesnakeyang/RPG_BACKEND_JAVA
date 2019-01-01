package com.gogoyang.rpgapi.business.user.login.service;

import com.gogoyang.rpgapi.framework.common.IRPGFunction;
import com.gogoyang.rpgapi.framework.constant.RoleType;
import com.gogoyang.rpgapi.meta.email.entity.Email;
import com.gogoyang.rpgapi.meta.email.service.IEmailService;
import com.gogoyang.rpgapi.meta.user.entity.User;
import com.gogoyang.rpgapi.meta.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserLoginBusinessService implements IUserLoginBusinessService{
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

    /**
     * login by email
     * @param in
     * email, password
     * @return
     * user
     * @throws Exception
     * 10024
     */
    @Override
    public Map loginByEmail(Map in) throws Exception {
        /**
         * read email by email
         * read user by email.userId
         * check the user.loginPassword
         * return user object
         */
        String emailName=in.get("email").toString();
        String password=in.get("password").toString();

        Email email=iEmailService.getEmailByEmail(emailName);
        if(email==null){
            throw new Exception("10024");
        }

        User user=iUserService.getUserByUserId(email.getUserId());
        if(user==null){
            throw new Exception("10024");
        }

        password=irpgFunction.encoderByMd5(password);
        if(!user.getLoginPassword().equals(password)){
            throw new Exception("10024");
        }

        Map out=new HashMap();

        out.put("userId", user.getUserId());
        out.put("token", user.getToken());
        out.put("email", email.getEmail());
        if(user.getRealName()!=null){
            out.put("username", user.getRealName());
        }else{
            out.put("username", user.getEmail());
        }
        out.put("roleType", RoleType.NORMAL);

        return out;
    }
}

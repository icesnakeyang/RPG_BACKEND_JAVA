package com.gogoyang.rpgapi.business.user.login.service;

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

    @Autowired
    public UserLoginBusinessService(IEmailService iEmailService,
                                    IUserService iUserService) {
        this.iEmailService = iEmailService;
        this.iUserService = iUserService;
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

        if(!user.getLoginPassword().equals(password)){
            throw new Exception("10024");
        }

        Map out=new HashMap();
        out.put("user", user);

        return out;
    }
}
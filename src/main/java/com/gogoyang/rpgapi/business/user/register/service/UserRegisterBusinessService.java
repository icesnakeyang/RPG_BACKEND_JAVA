package com.gogoyang.rpgapi.business.user.register.service;

import com.gogoyang.rpgapi.framework.common.IRPGFunction;
import com.gogoyang.rpgapi.meta.email.entity.Email;
import com.gogoyang.rpgapi.meta.email.service.IEmailService;
import com.gogoyang.rpgapi.meta.user.entity.User;
import com.gogoyang.rpgapi.meta.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class UserRegisterBusinessService implements IUserRegisterBusinessService{
    private final IEmailService iEmailService;
    private final IUserService iUserService;
    private final IRPGFunction irpgFunction;

    @Autowired
    public UserRegisterBusinessService(IEmailService iEmailService,
                                       IUserService iUserService,
                                       IRPGFunction irpgFunction) {
        this.iEmailService = iEmailService;
        this.iUserService = iUserService;
        this.irpgFunction = irpgFunction;
    }

    /**
     * Register user account with email
     * @param in
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public Map registerByEmail(Map in) throws Exception {
        /**
         * check the email
         * if occupied throw error
         * if not, create user and email
         * create user first, MD5 password, and generate a token
         * after get the userId, create email
         * return user map, include token and email
         */
        String emailName=in.get("email").toString();
        String password=in.get("loginPassword").toString();

        //check the mail
        Email email=iEmailService.getEmailByEmail(emailName);
        if(email!=null){
            throw new Exception("10044");
        }

        //create the user
        User user=new User();
        user.setLoginPassword(irpgFunction.encoderByMd5(password));
        user.setToken(UUID.randomUUID().toString().replace("-", ""));
        user.setRegisterTime(new Date());
        user.setTokenCreatedTime(new Date());
        user=iUserService.insert(user);

        email=new Email();
        email.setUserId(user.getUserId());
        email.setEmail(emailName);
        email.setCreatedTime(new Date());
        email.setCreatedUserId(user.getUserId());
        email.setUserId(user.getUserId());
        email.setDefault(true);
        iEmailService.insert(email);

        Map out=new HashMap();
        out.put("userId", user.getUserId());
        out.put("token", user.getToken());
        out.put("email", email.getEmail());
        return out;
    }
}

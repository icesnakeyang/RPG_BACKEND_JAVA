package com.gogoyang.rpgapi.business.user.userInfo;

import com.gogoyang.rpgapi.meta.email.entity.Email;
import com.gogoyang.rpgapi.meta.email.service.IEmailService;
import com.gogoyang.rpgapi.meta.realname.entity.RealName;
import com.gogoyang.rpgapi.meta.realname.service.IRealNameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserInfoService implements IUserInfoService{
    private final IRealNameService iRealNameService;
    private final IEmailService iEmailService;

    @Autowired
    public UserInfoService(IRealNameService iRealNameService,
                           IEmailService iEmailService) {
        this.iRealNameService = iRealNameService;
        this.iEmailService = iEmailService;
    }

    @Override
    public String getUserName(Integer userId) throws Exception {
        /**
         * 如果有实名就返回实名，没有实名就返回email
         */
        RealName realName= iRealNameService.getRealNameByUserId(userId);
        if(realName!=null){
            return realName.getRealName();
        }

        Email email=iEmailService.getDefaultEmailByUserId(userId);
        return email.getEmail();
    }
}

package com.gogoyang.rpgapi.business.user.userInfo;

import com.gogoyang.rpgapi.meta.email.entity.Email;
import com.gogoyang.rpgapi.meta.email.service.IEmailService;
import com.gogoyang.rpgapi.meta.phone.entity.Phone;
import com.gogoyang.rpgapi.meta.phone.service.IPhoneService;
import com.gogoyang.rpgapi.meta.realname.entity.RealName;
import com.gogoyang.rpgapi.meta.realname.service.IRealNameService;
import com.gogoyang.rpgapi.meta.user.entity.User;
import com.gogoyang.rpgapi.meta.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserInfoService implements IUserInfoService{
    private final IUserService iUserService;
    private final IRealNameService iRealNameService;
    private final IEmailService iEmailService;
    private final IPhoneService iPhoneService;

    @Autowired
    public UserInfoService(IRealNameService iRealNameService,
                           IEmailService iEmailService,
                           IUserService iUserService,
                           IPhoneService iPhoneService) {
        this.iRealNameService = iRealNameService;
        this.iEmailService = iEmailService;
        this.iUserService = iUserService;
        this.iPhoneService = iPhoneService;
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

    @Override
    public UserInfo getUserInfoByUserId(Integer userId) throws Exception {
        UserInfo userInfo=new UserInfo();
        User user=iUserService.getUserByUserId(userId);
        userInfo.setUser(user);

        Email email=iEmailService.getDefaultEmailByUserId(userId);
        userInfo.setEmail(email);

        Phone phone=iPhoneService.getDefaultPhoneByUserId(userId);
        userInfo.setPhone(phone);

        RealName realName=iRealNameService.getRealNameByUserId(userId);
        userInfo.setRealName(realName);

        return userInfo;
    }
}

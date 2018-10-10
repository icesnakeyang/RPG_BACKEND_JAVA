package com.gogoyang.rpgapi.business.user.info.service;

import com.gogoyang.rpgapi.meta.job.service.IJobService;
import com.gogoyang.rpgapi.meta.user.email.entity.Email;
import com.gogoyang.rpgapi.meta.user.email.service.IEmailService;
import com.gogoyang.rpgapi.meta.user.phone.service.IPhoneService;
import com.gogoyang.rpgapi.meta.user.realName.service.IRealNameService;
import com.gogoyang.rpgapi.meta.user.userInfo.entity.UserInfo;
import com.gogoyang.rpgapi.meta.user.userInfo.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Map;

@Service
public class UserInfoBusinessService implements IUserInfoBusinessService{
    private final IJobService iJobService;
    private final IEmailService iEmailService;
    private final IPhoneService iPhoneService;
    private final IRealNameService iRealNameService;
    private final IUserInfoService iUserInfoService;

    @Autowired
    public UserInfoBusinessService(IJobService iJobService, IEmailService iEmailService,
                                   IPhoneService iPhoneService, IRealNameService iRealNameService,
                                   IUserInfoService iUserInfoService) {
        this.iJobService = iJobService;
        this.iEmailService = iEmailService;
        this.iPhoneService = iPhoneService;
        this.iRealNameService = iRealNameService;
        this.iUserInfoService = iUserInfoService;
    }

    @Override
    public void saveUserContactInfo(Map in) throws Exception {
        /**
         * 用户在申请任务时会提交一个联系信息，包括email, phone, realname
         * 分别保存并记录日志
         */
        String email=in.get("email").toString();
        String phone=in.get("phone").toString();
        String realName=in.get("realName").toString();
        String token=in.get("token").toString();

        UserInfo userInfo=iUserInfoService.loadUserByToken(token);
        if(userInfo==null){
            throw new Exception("10004");
        }

        /**
         * 保存Email
         * 1、检查当前用户的所有email，看是否已存在。
         * 2、如果不存在，检查是否被其他用户使用。
         * 3、新增email。
         * 4、把当前用户的所有其他email的default设置为false。
         * 5、把当前email的default设置为true。
         */
        iEmailService.createEmail()
        if(email!=null){
            if(!userInfo.getEmail().equals(email)){
                Email theEmail=new Email();
                theEmail.setEmail(email);
                theEmail.setUserId(userInfo.getUserId());
                theEmail.setCreatedTime(new Date());
                theEmail.setCreatedUserId(userInfo.getUserId());
                theEmail.setDefault(true);
            }
        }
    }
}

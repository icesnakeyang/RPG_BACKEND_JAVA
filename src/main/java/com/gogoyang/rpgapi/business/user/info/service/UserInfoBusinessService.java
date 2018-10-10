package com.gogoyang.rpgapi.business.user.info.service;

import com.gogoyang.rpgapi.meta.job.entity.Job;
import com.gogoyang.rpgapi.meta.job.service.IJobService;
import com.gogoyang.rpgapi.meta.user.email.entity.Email;
import com.gogoyang.rpgapi.meta.user.email.service.IEmailService;
import com.gogoyang.rpgapi.meta.user.phone.service.IPhoneService;
import com.gogoyang.rpgapi.meta.user.realName.service.IRealNameService;
import com.gogoyang.rpgapi.meta.user.userInfo.entity.UserInfo;
import com.gogoyang.rpgapi.meta.user.userInfo.service.IUserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
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
    @Transactional(rollbackOn = Exception.class)
    public void saveUserContactInfo(Map in) throws Exception {
        /**
         * 用户在申请任务时会提交一个联系信息，包括email, phone, realname
         * 分别保存并记录日志
         */
        String strEmail=in.get("email").toString();
        String phone=in.get("phone").toString();
        String realName=in.get("realName").toString();
        String token=in.get("token").toString();

        UserInfo userInfo=iUserInfoService.loadUserByToken(token);
        if(userInfo==null){
            throw new Exception("10004");
        }

        /**
         * 保存Email
         * 检查数据库里是否已经有该email。
         *      有：检查是否被当前用户使用。
         *          是：是否默认
         *              是：do nothing
         *              否：default=true，把其他的default=false
         *          否：返回错误，10044
         *       否：查询当前用户的所有Email
         *          是否有其他email
         *              有：全部default=false
         *       新增email
         *       把default=true
         */
        Email email=iEmailService.loadEmailByEmail(strEmail);
        if(email!=null) {
            if (email.getUserId() == userInfo.getUserId()) {
                if (email.getDefault()) {
                    return;
                } else {
                    ArrayList<Email> myEmails = iEmailService.loadEmailByUserId(userInfo.getUserId());
                    for (int i = 0; i < myEmails.size(); i++) {
                        if (!myEmails.get(i).getEmail().equals(strEmail)) {
                            myEmails.get(i).setDefault(false);
                            iEmailService.updateEmail(myEmails.get(i));
                        } else {
                            myEmails.get(i).setDefault(true);
                            iEmailService.updateEmail(myEmails.get(i));
                        }
                    }
                    return;
                }
            }
                throw new Exception("10044");
        }

        ArrayList<Email> myEmails = iEmailService.loadEmailByUserId(userInfo.getUserId());
        for (int i = 0; i < myEmails.size(); i++) {
            if (!myEmails.get(i).getEmail().equals(strEmail)) {
                myEmails.get(i).setDefault(false);
                iEmailService.updateEmail(myEmails.get(i));
            }
        }
        email=new Email();
        email.setEmail(strEmail);
        email.setDefault(true);
        email.setCreatedTime(new Date());
        email.setCreatedUserId(userInfo.getUserId());
        iEmailService.insertEmail(email);



                    userInfo.setEmail(strEmail);
                    iUserInfoService.updateUser(userInfo);
    }

    @Override
    public void saveEmail(Map in) throws Exception {

    }
}

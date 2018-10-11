package com.gogoyang.rpgapi.business.user.info.service;

import com.gogoyang.rpgapi.meta.job.service.IJobService;
import com.gogoyang.rpgapi.meta.user.email.entity.Email;
import com.gogoyang.rpgapi.meta.user.email.service.IEmailService;
import com.gogoyang.rpgapi.meta.user.phone.entity.Phone;
import com.gogoyang.rpgapi.meta.user.phone.service.IPhoneService;
import com.gogoyang.rpgapi.meta.user.realName.entity.RealName;
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
public class UserInfoBusinessService implements IUserInfoBusinessService {
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

    /**
     * 同时保存用户的realname， email， phone等信息，并设置为默认。
     *
     * @param in
     * @throws Exception
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public void saveUserContactInfo(Map in) throws Exception {
        /**
         * 用户在申请任务时会提交一个联系信息，包括email, phone, realname
         * 分别保存并记录日志
         */
        String strEmail = null;
        if (in.get("email") != null) {
            strEmail = in.get("email").toString();
        }
        String strPhone = null;
        if (in.get("phone") != null) {
            strPhone = in.get("phone").toString();
        }
        String strRealName = null;
        if (in.get("realName") != null) {
            strRealName = in.get("realName").toString();
        }
        String token = in.get("token").toString();

        UserInfo userInfo = iUserInfoService.loadUserByToken(token);
        if (userInfo == null) {
            throw new Exception("10004");
        }

        if (strEmail != null && !strEmail.equals("")) {
            saveEmail(strEmail, userInfo);
            userInfo.setEmail(strEmail);
        }
        if (strPhone != null && !strPhone.equals("")) {
            savePhone(strPhone, userInfo);
            userInfo.setPhone(strPhone);
        }
        if (strRealName != null && !strRealName.equals("")) {
            saveRealName(strRealName, userInfo);
            userInfo.setRealName(strRealName);
        }
        iUserInfoService.updateUser(userInfo);
    }

    /**
     * save email
     * will handle set this email to default
     * this function will not update the job
     *
     * @param strEmail
     * @param userInfo
     * @throws Exception
     */
    @Override
    public void saveEmail(String strEmail, UserInfo userInfo) throws Exception {
        /**
         * 保存Email
         *  * 检查数据库里是否已经有该email。
         *  *      有：检查是否被当前用户使用。
         *  *          是：是否默认
         *  *              是：do nothing
         *  *              否：default=true，把其他的default=false
         *  *          否：返回错误，10044
         *  *       否：查询当前用户的所有Email
         *  *          是否有其他email
         *  *              有：全部default=false
         *  *       新增email
         *  *       把default=true
         */
        Email email = iEmailService.loadEmailByEmail(strEmail);
        if (email != null) {
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
        email = new Email();
        email.setEmail(strEmail);
        email.setDefault(true);
        email.setCreatedTime(new Date());
        email.setCreatedUserId(userInfo.getUserId());
        iEmailService.insertEmail(email);
    }

    @Override
    public void savePhone(String strPhone, UserInfo userInfo) throws Exception {
        /**
         * 保存Phone
         *  * 检查数据库里是否已经有该phone
         *  *      有：检查是否被当前用户使用。
         *  *          是：是否默认
         *  *              是：do nothing
         *  *              否：default=true，把其他的default=false
         *  *          否：返回错误，10044
         *  *       否：查询当前用户的所有phone
         *  *          是否有其他phone
         *  *              有：全部default=false
         *  *       新增phone
         *  *       把default=true
         */
        Phone phone = iPhoneService.loadPhoneByPhone(strPhone);
        if (phone != null) {
            if (phone.getUserId() == userInfo.getUserId()) {
                if (phone.getDefault()) {
                    return;
                } else {
                    ArrayList<Phone> myPhones = iPhoneService.loadPhoneByUserId(userInfo.getUserId());
                    for (int i = 0; i < myPhones.size(); i++) {
                        if (!myPhones.get(i).getPhone().equals(strPhone)) {
                            myPhones.get(i).setDefault(false);
                            iPhoneService.updatePhone(myPhones.get(i));
                        } else {
                            myPhones.get(i).setDefault(true);
                            iPhoneService.updatePhone(myPhones.get(i));
                        }
                    }
                    return;
                }
            }
            throw new Exception("10046");
        }

        ArrayList<Phone> myPhones = iPhoneService.loadPhoneByUserId(userInfo.getUserId());
        for (int i = 0; i < myPhones.size(); i++) {
            if (!myPhones.get(i).getPhone().equals(strPhone)) {
                myPhones.get(i).setDefault(false);
                iPhoneService.updatePhone(myPhones.get(i));
            }
        }
        phone = new Phone();
        phone.setPhone(strPhone);
        phone.setDefault(true);
        phone.setCreatedTime(new Date());
        phone.setCreatedUserId(userInfo.getUserId());
        iPhoneService.insertPhone(phone);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void saveRealName(String strRealName, UserInfo userInfo) throws Exception {
        RealName realName = iRealNameService.loadCurrentRealName(userInfo.getUserId());
        if (realName == null) {
            //如果为null，则说明用户当前没有实名，直接添加一个就行了
            realName = new RealName();
            realName.setCreatedTime(new Date());
            realName.setRealName(strRealName);
            realName.setUserId(userInfo.getUserId());
            iRealNameService.insertRealName(realName);
            return;
        } else {
            //如果不为空，则将现有实名设置失效，然后再添加一个。
            //如意实名如果相同，就不处理了。
            if (realName.getRealName().equals(strRealName)) {
                return;
            }
            realName.setDisableTime(new Date());
            iRealNameService.updateRealName(realName);
            realName = new RealName();
            realName.setCreatedTime(new Date());
            realName.setRealName(strRealName);
            realName.setUserId(userInfo.getUserId());
            iRealNameService.insertRealName(realName);
            return;
        }
    }
}

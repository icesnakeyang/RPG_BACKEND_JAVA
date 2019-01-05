package com.gogoyang.rpgapi.business.user.profile.service;

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

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class ProfileBusinessService implements IProfileBusinessService {
    private final IUserService iUserService;
    private final IEmailService iEmailService;
    private final IPhoneService iPhoneService;
    private final IRealNameService iRealNameService;

    @Autowired
    public ProfileBusinessService(IEmailService iEmailService,
                                  IUserService iUserService,
                                  IPhoneService iPhoneService,
                                  IRealNameService iRealNameService) {
        this.iEmailService = iEmailService;
        this.iUserService = iUserService;
        this.iPhoneService = iPhoneService;
        this.iRealNameService = iRealNameService;
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

        User user = iUserService.getUserByToken(token);
        if (user == null) {
            throw new Exception("10004");
        }

        if (strEmail != null && !strEmail.equals("")) {
            saveEmail(strEmail, user);
        }
        if (strPhone != null && !strPhone.equals("")) {
            savePhone(strPhone, user);
        }
        if (strRealName != null && !strRealName.equals("")) {
            saveRealName(strRealName, user);
        }
    }

    @Override
    public Map getUserInfo(Map in) throws Exception {
        String token = in.get("token").toString();
        User user = iUserService.getUserByToken(token);
        Map out = new HashMap();
        out.put("user", user);
        return out;
    }

    /**
     * save email
     * will handle set this email to default
     * this function will not update the common
     */
    @Transactional(rollbackOn = Exception.class)
    protected void saveEmail(String strEmail, User user) throws Exception {
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
        //读取数据库里是否有该邮箱
        Email email = iEmailService.getEmailByEmail(strEmail);
        if (email != null) {
            //数据库里有该邮箱
            if (email.getUserId() == user.getUserId()) {
                //是当前用户
                if (email.getDefault()) {
                    //是默认邮箱，无需处理
                    return;
                } else {
                    //不是默认邮箱，读取该用户下所有邮箱
                    ArrayList<Email> myEmails = iEmailService.listEmailByUserId(user.getUserId());
                    for (int i = 0; i < myEmails.size(); i++) {
                        if (!myEmails.get(i).getEmail().equals(strEmail)) {
                            //如果原邮箱不等于当前邮箱，设置为非默认
                            myEmails.get(i).setDefault(false);
                            iEmailService.updateEmail(myEmails.get(i));
                        } else {
                            //是当前邮箱，设置为默认
                            myEmails.get(i).setDefault(true);
                            iEmailService.updateEmail(myEmails.get(i));

                            //修改user的默认email
                            if (!user.getEmail().equals(myEmails.get(i).getEmail())) {
                                user.setEmail(myEmails.get(i).getEmail());
                                iUserService.update(user);
                            }
                        }
                    }
                    return;
                }
            }
            //不是当前用户，即该邮箱被其他用户使用了，报错
            throw new Exception("10044");
        }

        //新的邮箱，先读取当前用户下的邮箱，设置默认为false，然后新增该邮箱，设置默认为true
        ArrayList<Email> myEmails = iEmailService.listEmailByUserId(user.getUserId());
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
        email.setUserId(user.getUserId());
        email.setCreatedUserId(user.getUserId());
        iEmailService.insert(email);
        user.setEmail(email.getEmail());
        iUserService.update(user);
    }

    @Transactional(rollbackOn = Exception.class)
    protected void savePhone(String strPhone, User user) throws Exception {
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
        Phone phone = iPhoneService.getPhoneByPhone(strPhone);
        if (phone != null) {
            if (phone.getUserId() == user.getUserId()) {
                if (phone.getDefault()) {
                    return;
                } else {
                    ArrayList<Phone> myPhones = iPhoneService.listPhoneByUserId(user.getUserId());
                    for (int i = 0; i < myPhones.size(); i++) {
                        if (!myPhones.get(i).getPhone().equals(strPhone)) {
                            myPhones.get(i).setDefault(false);
                            iPhoneService.updatePhone(myPhones.get(i));
                        } else {
                            myPhones.get(i).setDefault(true);
                            iPhoneService.updatePhone(myPhones.get(i));
                            user.setPhone(strPhone);
                            iUserService.update(user);
                        }
                    }
                    return;
                }
            }
            throw new Exception("10046");
        }

        ArrayList<Phone> myPhones = iPhoneService.listPhoneByUserId(user.getUserId());
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
        phone.setUserId(user.getUserId());
        phone.setCreatedUserId(user.getUserId());
        iPhoneService.insert(phone);
        user.setPhone(phone.getPhone());
        iUserService.update(user);
    }

    @Transactional(rollbackOn = Exception.class)
    protected void saveRealName(String strRealName, User user) throws Exception {
        /**
         * 查询用户是否已经有认证的实名，如果有就退出。
         * 如果没有就直接修改实名
         */
        RealName realName = iRealNameService.getRealNameByUserId(user.getUserId());
        if (realName == null) {
            //如果为null，则说明用户当前没有实名，直接添加一个就行了
            realName = new RealName();
            realName.setCreatedTime(new Date());
            realName.setRealName(strRealName);
            realName.setUserId(user.getUserId());
            iRealNameService.insert(realName);
            user.setRealName(realName.getRealName());
            iUserService.update(user);
        } else {
            //如果不为空，则检查用户实名是否认证，若未认证则则直接修改
            if (realName.getRealName().equals(strRealName)) {
                return;
            }
            if (realName.getVerified() != null) {
                if (realName.getVerified()) {
                    throw new Exception("10117");
                }
            }
            realName.setCreatedTime(new Date());
            realName.setRealName(strRealName);
            realName.setUserId(user.getUserId());
            iRealNameService.update(realName);
            user.setRealName(strRealName);
            iUserService.update(user);
        }
    }
}

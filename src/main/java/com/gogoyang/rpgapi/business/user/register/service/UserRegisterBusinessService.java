package com.gogoyang.rpgapi.business.user.register.service;

import com.gogoyang.rpgapi.framework.common.IRPGFunction;
import com.gogoyang.rpgapi.framework.constant.LogStatus;
import com.gogoyang.rpgapi.framework.constant.RoleType;
import com.gogoyang.rpgapi.meta.email.entity.Email;
import com.gogoyang.rpgapi.meta.email.service.IEmailService;
import com.gogoyang.rpgapi.meta.phone.entity.Phone;
import com.gogoyang.rpgapi.meta.phone.service.IPhoneService;
import com.gogoyang.rpgapi.meta.sms.entity.SMSLog;
import com.gogoyang.rpgapi.meta.sms.service.ISMSLogService;
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
public class UserRegisterBusinessService implements IUserRegisterBusinessService {
    private final IEmailService iEmailService;
    private final IUserService iUserService;
    private final IRPGFunction irpgFunction;
    private final IPhoneService iPhoneService;
    private final ISMSLogService ismsLogService;

    @Autowired
    public UserRegisterBusinessService(IEmailService iEmailService,
                                       IUserService iUserService,
                                       IRPGFunction irpgFunction,
                                       IPhoneService iPhoneService,
                                       ISMSLogService ismsLogService) {
        this.iEmailService = iEmailService;
        this.iUserService = iUserService;
        this.irpgFunction = irpgFunction;
        this.iPhoneService = iPhoneService;
        this.ismsLogService = ismsLogService;
    }

    /**
     * Register user account with email
     *
     * @param in
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public Map registerByEmail(Map in) throws Exception {

    }

    @Override
    public Map getEmailByEmail(Map in) throws Exception {
        String emailStr = in.get("email").toString();
        Email email = iEmailService.getEmailByEmail(emailStr);
        Map out = new HashMap();
        out.put("email", email);
        return out;
    }

    @Override
    public Map getPhone(Map in) throws Exception {
        String phoneNumber = in.get("phone").toString();

        User user = iUserService.getUserByPhone(phoneNumber);

        Map out = new HashMap();
        out.put("user", user);
        return out;
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public Map registerByPhone(Map in) throws Exception {
        String phoneStr = in.get("phone").toString();
        String code = in.get("code").toString();
        String password = in.get("loginPassword").toString();
        String realName = (String) in.get("realName");

        //check the phone
        User user = iUserService.getUserByPhone(phoneStr);
        if (user != null) {
            throw new Exception("10046");
        }

        //check code
        irpgFunction.verifyMSMCode(phoneStr,code);

        //create the user
        user = new User();
        user.setLoginPassword(irpgFunction.encoderByMd5(password));
        user.setToken(UUID.randomUUID().toString().replace("-", ""));
        user.setRegisterTime(new Date());
        user.setTokenCreatedTime(new Date());
        user.setPhone(phoneStr);
        user.setRealName(realName);
        user = iUserService.insert(user);

        Phone phone = new Phone();
        phone.setDefault(true);
        phone.setCreatedTime(new Date());
        phone.setUserId(user.getUserId());
        phone.setCreatedUserId(user.getUserId());
        phone.setPhone(phoneStr);
        phone.setVerify(true);
        phone.setVerifyTime(new Date());

        iPhoneService.insert(phone);

        /**
         * 把smslog设置失效
         */
        SMSLog smsLog=ismsLogService.getSMSLog(phoneStr,code);
        smsLog.setStatus(LogStatus.OVERDUE.toString());
        ismsLogService.updateSMSLog(smsLog);

        Map out = new HashMap();
        out.put("userId", user.getUserId());
        out.put("token", user.getToken());
        out.put("phone", phone.getPhone());
        if (realName != null) {
            out.put("username", realName);
        } else {
            out.put("username", phoneStr);
        }
        out.put("roleType", RoleType.NORMAL);
        return out;
    }
}

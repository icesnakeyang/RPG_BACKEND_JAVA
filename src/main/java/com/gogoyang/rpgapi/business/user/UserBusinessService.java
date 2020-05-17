package com.gogoyang.rpgapi.business.user;

import com.gogoyang.rpgapi.framework.common.GogoTools;
import com.gogoyang.rpgapi.framework.common.IRPGFunction;
import com.gogoyang.rpgapi.framework.constant.LogStatus;
import com.gogoyang.rpgapi.framework.constant.RoleType;
import com.gogoyang.rpgapi.meta.email.entity.Email;
import com.gogoyang.rpgapi.meta.email.service.IEmailService;
import com.gogoyang.rpgapi.meta.phone.entity.Phone;
import com.gogoyang.rpgapi.meta.phone.service.IPhoneService;
import com.gogoyang.rpgapi.meta.sms.entity.SMSLog;
import com.gogoyang.rpgapi.meta.sms.service.ISMSLogService;
import com.gogoyang.rpgapi.meta.user.entity.UserInfo;
import com.gogoyang.rpgapi.meta.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserBusinessService implements IUserBusinessService {
    private final IEmailService iEmailService;
    private final IUserService iUserService;
    private final IPhoneService iPhoneService;
    private final IRPGFunction irpgFunction;
    @Autowired
    private ISMSLogService ismsLogService;

    public UserBusinessService(IEmailService iEmailService,
                               IUserService iUserService,
                               IPhoneService iPhoneService,
                               IRPGFunction irpgFunction,
                               ISMSLogService ismsLogService) {
        this.iEmailService = iEmailService;
        this.iUserService = iUserService;
        this.iPhoneService = iPhoneService;
        this.irpgFunction = irpgFunction;
        this.ismsLogService = ismsLogService;
    }

    /**
     * Login by email or phone
     *
     * @param in
     * @return
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map login(Map in) throws Exception {
        String loginName = in.get("loginName").toString();
        String password = in.get("password").toString();

        UserInfo user = null;
        if (irpgFunction.checkEmail(loginName)) {
            Email email = iEmailService.getEmailByEmail(loginName);
            if (email != null) {
                user = iUserService.getUserByUserId(email.getUserId());
            }
        } else {
            Phone phone = iPhoneService.getPhoneByPhone(loginName);
            if (phone != null) {
                user = iUserService.getUserByUserId(phone.getUserId());
            }
        }
        if (user == null) {
            throw new Exception("10024");
        }

        password = irpgFunction.encoderByMd5(password);
        if (!user.getLoginPassword().equals(password)) {
//            throw new Exception("10024");
        }

        Map out = new HashMap();

        out.put("userId", user.getUserId());
        out.put("token", user.getToken());
        out.put("email", user.getEmail());
        if (user.getRealName() != null) {
            out.put("username", user.getRealName());
        } else {
            out.put("username", user.getEmail());
        }
        out.put("phone", user.getPhone());
        out.put("roleType", RoleType.NORMAL);

        return out;
    }

    /**
     * Register by email or phone
     *
     * @param in
     * @return
     * @throws Exception
     */
    @Override
    public Map registerByEmail(Map in) throws Exception {
        String email = in.get("email").toString();
        String password = in.get("password").toString();
        String realName = in.get("realName").toString();

        //check the email
        if (irpgFunction.checkEmail(email)) {
            //是email
            Email oldEmail = iEmailService.getEmailByEmail(email);
            //email已经被注册了
            if (oldEmail != null) {
                throw new Exception("10044");
            }
        } else {
            //不是email，那就默认为phone

        }

        //create the user
        UserInfo user = new UserInfo();
        user.setUserId(GogoTools.UUID());
        user.setLoginPassword(irpgFunction.encoderByMd5(password));
        user.setToken(GogoTools.UUID());
        user.setRegisterTime(new Date());
        user.setTokenCreatedTime(new Date());
        user.setEmail(email);
        user.setRealName(realName);
        iUserService.createUserInfo(user);

        //create email
        Email theEmail = new Email();
        theEmail.setEmailId(GogoTools.UUID());
        theEmail.setUserId(user.getUserId());
        theEmail.setEmail(email);
        theEmail.setCreatedTime(new Date());
        theEmail.setCreatedUserId(user.getUserId());
        theEmail.setUserId(user.getUserId());
        iEmailService.insert(theEmail);

        Map out = new HashMap();
        out.put("userId", user.getUserId());
        out.put("token", user.getToken());
        out.put("email", email);
        out.put("username", realName);
        return out;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Map registerByPhone(Map in) throws Exception {
        String phoneStr = in.get("phone").toString();
        String code = in.get("code").toString();
        String password = in.get("password").toString();
        String realName = in.get("realName").toString();

        //check the phone
        UserInfo user = iUserService.getUserByPhone(phoneStr);
        if (user != null) {
            throw new Exception("10046");
        }

        //check code
        irpgFunction.verifyMSMCode(phoneStr, code);

        //create the user
        user = new UserInfo();
        user.setUserId(GogoTools.UUID());
        user.setLoginPassword(irpgFunction.encoderByMd5(password));
        user.setToken(GogoTools.UUID());
        user.setRegisterTime(new Date());
        user.setTokenCreatedTime(new Date());
        user.setPhone(phoneStr);
        user.setRealName(realName);
        iUserService.createUserInfo(user);

        Phone phone = new Phone();
        phone.setIsDefault(true);
        phone.setCreatedTime(new Date());
        phone.setUserId(user.getUserId());
        phone.setCreatedUserId(user.getUserId());
        phone.setPhone(phoneStr);
        phone.setIsVerify(true);
        phone.setVerifyTime(new Date());

        iPhoneService.insert(phone);

        /**
         * 把smslog设置失效
         */
        SMSLog smsLog = ismsLogService.getSMSLog(phoneStr, code);
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

    @Override
    public Map getPhone(Map in) throws Exception {
        String phoneNumber = in.get("phone").toString();

        UserInfo user = iUserService.getUserByPhone(phoneNumber);

        Map out = new HashMap();
        out.put("user", user);
        return out;
    }
}
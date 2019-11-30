package com.gogoyang.rpgapi.business.user;

import com.gogoyang.rpgapi.framework.common.IRPGFunction;
import com.gogoyang.rpgapi.framework.constant.RoleType;
import com.gogoyang.rpgapi.meta.email.entity.Email;
import com.gogoyang.rpgapi.meta.email.service.IEmailService;
import com.gogoyang.rpgapi.meta.phone.entity.Phone;
import com.gogoyang.rpgapi.meta.phone.service.IPhoneService;
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
public class UserBusinessService implements IUserBusinessService {
    private final IEmailService iEmailService;
    private final IUserService iUserService;
    private final IPhoneService iPhoneService;
    private final IRPGFunction irpgFunction;

    public UserBusinessService(IEmailService iEmailService,
                               IUserService iUserService,
                               IPhoneService iPhoneService,
                               IRPGFunction irpgFunction) {
        this.iEmailService = iEmailService;
        this.iUserService = iUserService;
        this.iPhoneService = iPhoneService;
        this.irpgFunction = irpgFunction;
    }

    /**
     * Login by email or phone
     *
     * @param in
     * @return
     * @throws Exception
     */
    @Transactional(rollbackOn = Exception.class)
    @Override
    public Map login(Map in) throws Exception {
        String emailName = (String) in.get("email");
        String phoneStr = (String) in.get("phone");
        String password = in.get("password").toString();

        User user = null;
        if (emailName != null) {
            Email email = iEmailService.getEmailByEmail(emailName);
            if (email != null) {
                user = iUserService.getUserByUserId(email.getUserId());
            }
        }
        if (user == null) {
            if (phoneStr != null) {
                Phone phone = iPhoneService.getPhoneByPhone(phoneStr);
                if (phone != null) {
                    user = iUserService.getUserByUserId(phone.getUserId());
                }
            }
        }
        if (user == null) {
            throw new Exception("10024");
        }

        password = irpgFunction.encoderByMd5(password);
        if (!user.getLoginPassword().equals(password)) {
            throw new Exception("10024");
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
     * @param in
     * @return
     * @throws Exception
     */
    @Override
    public Map register(Map in) throws Exception {
        String loginName = in.get("loginName").toString();
        String password = in.get("loginPassword").toString();
        String realName = (String) in.get("realName");

        String email = null;
        String phone = null;
        //check the email
        if (irpgFunction.checkEmail(loginName)) {
            //是email
            Email oldEmail = iEmailService.getEmailByEmail(loginName);
            //email已经被注册了
            if (oldEmail != null) {
                throw new Exception("10044");
            }
            email = loginName;
        } else {
            //不是email，那就默认为phone
            Phone thePhone = iPhoneService.getPhoneByPhone(loginName);
            if (thePhone != null) {
                //phone已经被注册了
                throw new Exception("10046");
            }
            phone = loginName;
        }

        //create the user
        User user = new User();
        user.setLoginPassword(irpgFunction.encoderByMd5(password));
        user.setToken(UUID.randomUUID().toString().replace("-", ""));
        user.setRegisterTime(new Date());
        user.setTokenCreatedTime(new Date());
        user.setEmail(email);
        user.setPhone(phone);
        user.setRealName(realName);
        user = iUserService.insert(user);

        if (email != null) {
            Email theEmail = new Email();
            theEmail.setUserId(user.getUserId());
            theEmail.setEmail(email);
            theEmail.setCreatedTime(new Date());
            theEmail.setCreatedUserId(user.getUserId());
            theEmail.setUserId(user.getUserId());
            theEmail.setDefault(true);
            iEmailService.insert(theEmail);
        }

        if (phone != null) {
            Phone thePhone = new Phone();
            thePhone.setDefault(true);
            thePhone.setCreatedTime(new Date());
            thePhone.setUserId(user.getUserId());
            thePhone.setCreatedUserId(user.getUserId());
            thePhone.setPhone(phone);
            thePhone.setVerify(true);
            thePhone.setVerifyTime(new Date());
            iPhoneService.insert(thePhone);
        }

        Map out = new HashMap();
        out.put("userId", user.getUserId());
        out.put("token", user.getToken());
        out.put("email", email);
        out.put("phone", phone);
        if (realName != null) {
            out.put("username", realName);
        } else {
            if (phone != null) {
                out.put("username", phone);
            } else {
                if (email != null) {
                    out.put("username", email);
                }
            }
        }
        out.put("roleType", RoleType.NORMAL);
        return out;
    }
}
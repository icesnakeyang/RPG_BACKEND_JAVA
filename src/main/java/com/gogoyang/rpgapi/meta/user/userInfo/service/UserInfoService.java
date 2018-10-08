package com.gogoyang.rpgapi.meta.user.userInfo.service;

import com.gogoyang.rpgapi.framework.common.IRPGFunction;
import com.gogoyang.rpgapi.meta.apply.service.IJobApplyService;
import com.gogoyang.rpgapi.meta.user.email.entity.Email;
import com.gogoyang.rpgapi.meta.user.email.service.IEmailService;
import com.gogoyang.rpgapi.meta.user.phone.entity.Phone;
import com.gogoyang.rpgapi.meta.user.phone.service.IPhoneService;
import com.gogoyang.rpgapi.meta.user.realName.entity.RealName;
import com.gogoyang.rpgapi.meta.user.realName.service.IRealNameService;
import com.gogoyang.rpgapi.meta.user.userInfo.dao.UserInfoDao;
import com.gogoyang.rpgapi.meta.user.userInfo.entity.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.UUID;

@Service
public class UserInfoService implements IUserInfoService {
    private final IRPGFunction iRPGFunction;
    private final UserInfoDao userInfoDao;
    private final IJobApplyService iJobApplyService;
    private final IEmailService iEmailService;
    private final IPhoneService iPhoneService;
    private final IRealNameService iRealNameService;

    @Autowired
    public UserInfoService(IRPGFunction iRPGFunction, UserInfoDao userInfoDao, IJobApplyService iJobApplyService, IEmailService iEmailService, IPhoneService iPhoneService, IRealNameService iRealNameService) {
        this.iRPGFunction = iRPGFunction;
        this.userInfoDao = userInfoDao;
        this.iJobApplyService = iJobApplyService;
        this.iEmailService = iEmailService;
        this.iPhoneService = iPhoneService;
        this.iRealNameService = iRealNameService;
    }

    /**
     * 创建一个新用户，返回带userId的userInfo
     *
     * @param userInfo
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public UserInfo createUser(UserInfo userInfo) throws Exception {
        if (userInfo.getUsername().equals("")) {
            throw new Exception("10010");
        }
        //把密码进行MD5加密
        userInfo.setPassword(iRPGFunction.encoderByMd5(userInfo.getPassword()));
        userInfo.setRegisterTime(new Date());
        //生成该用户的Token
        userInfo.setToken(UUID.randomUUID().toString().replace("-", ""));
        //保存用户后自动返回userId
        userInfo.setUserId(userInfoDao.save(userInfo).getUserId());
        return userInfo;
    }

    /**
     * 根据username查询userinfo
     *
     * @param username
     * @return
     * @throws Exception
     */
    @Override
    public UserInfo loadUserByUsername(String username) throws Exception {
        UserInfo userInfo = userInfoDao.findByUsername(username);
        return userInfo;
    }

    /**
     * 根据token读取userInfo
     *
     * @param token
     * @return
     * @throws Exception
     */
    @Override
    public UserInfo loadUserByToken(String token) throws Exception {
        UserInfo userInfo= userInfoDao.findByToken(token);
        return userInfo;
    }

    /**
     * 根据userId读取UserInfo
     *
     * @param userId
     * @return
     * @throws Exception
     */
    @Override
    public UserInfo loadUserByUserId(Integer userId) throws Exception {
        UserInfo userInfo = userInfoDao.findByUserId(userId);
        return userInfo;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void updateUser(UserInfo userInfo) throws Exception {
        UserInfo fixUser=userInfoDao.findByUserId(userInfo.getUserId());
        if (fixUser == null) {
            throw new Exception("10027");
        }
        //todo
        /**
         * 这里写一个遍历userInfo的方法，如果属性不为空，或者不为默认值，则修改。
         * here need to add code, iterate all the UserInfo properties,
         * if the property is not null, or not default value, then update it to fixUser, save it.
         */
        if(userInfo.getPhone()!=null){
            fixUser.setPhone(userInfo.getPhone());
        }
        if(fixUser.getHonor()!=userInfo.getHonor()){
            fixUser.setHonor(userInfo.getHonor());
        }
        if(fixUser.getHonorIn()!=userInfo.getHonorIn()){
            fixUser.setHonorIn(userInfo.getHonorIn());
        }
        if(fixUser.getHonorOut()!=userInfo.getHonorOut()){
            fixUser.setHonorOut(userInfo.getHonorOut());
        }

        userInfoDao.save(fixUser);
    }

    /**
     * 一次性保存email，phone，realname
     * @param userInfo
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public void saveContactInfo(UserInfo userInfo) throws Exception{
        UserInfo newUserInfo=userInfoDao.findByUserId(userInfo.getUserId());

        //保存phone
        if(userInfo.getEmail()!=null){
            //创建一个email记录
            Email email=new Email();
            email.setUserId(userInfo.getUserId());
            email.setEmail(userInfo.getEmail());
            email.setDefault(true);
            iEmailService.createEmail(email);
            newUserInfo.setEmail(email.getEmail());
        }

        //保存email
        if(userInfo.getPhone()!=null){
            Phone phone=new Phone();
            phone.setUserId(userInfo.getUserId());
            phone.setPhone(userInfo.getPhone());
            phone.setDefault(true);
            iPhoneService.createPhone(phone);
            newUserInfo.setPhone(phone.getPhone());
        }

        //保存real name
        if(userInfo.getRealName()!=null){
            RealName realName=new RealName();
            realName.setUserId(userInfo.getUserId());
            realName.setRealName(userInfo.getRealName());
            realName.setCreatedTime(new Date());
            iRealNameService.createName(realName);
            newUserInfo.setRealName(realName.getRealName());
        }

        //保存UserInfo
        userInfoDao.save(newUserInfo);
    }

    /**
     * 检查token是否有效
     * 根据token去查询user用户，如果查到了user，返回user
     * @param token
     * @return
     */
    @Override
    public UserInfo checkToken(String token) throws Exception{
        if (token == null) {
            return null;
        }
        UserInfo userInfo = loadUserByToken(token);
        return userInfo;
    }

    /**
     * createdUserName从userInfo表里读取，
     * 如果用户有realName，则为realName
     * 如果没有realName则为email，
     * 如果没有email，则为phone
     * 如果没有phone，则为username
     * @param userId
     * @return
     * @throws Exception
     */
    public String getUserName(Integer userId) throws Exception{
        UserInfo userInfo=loadUserByUserId(userId);
        if(userInfo==null){
            return null;
        }
        if(userInfo.getRealName()!=null){
            return userInfo.getRealName();
        }
        if(userInfo.getEmail()!=null){
            return userInfo.getEmail();
        }
        if(userInfo.getPhone()!=null){
            return userInfo.getPhone();
        }
        return userInfo.getUsername();
    }

    /**
     * 设置userinfo.phone为default
     * @param userInfo
     * @throws Exception
     */
    @Override
    @Transactional(rollbackOn = Exception.class)
    public void setDefaultPhone(UserInfo userInfo) throws Exception{
        iPhoneService.setPhoneDefault(userInfo.getPhone());
        UserInfo fixUserInfo=loadUserByUserId(userInfo.getUserId());
        fixUserInfo.setPhone(userInfo.getPhone());
        userInfoDao.save(fixUserInfo);
    }
}

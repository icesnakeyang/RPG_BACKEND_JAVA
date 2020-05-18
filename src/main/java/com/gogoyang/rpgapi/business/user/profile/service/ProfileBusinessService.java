package com.gogoyang.rpgapi.business.user.profile.service;

import com.gogoyang.rpgapi.framework.common.GogoTools;
import com.gogoyang.rpgapi.framework.common.ICommonBusinessService;
import com.gogoyang.rpgapi.framework.constant.GenderType;
import com.gogoyang.rpgapi.framework.constant.GogoStatus;
import com.gogoyang.rpgapi.framework.constant.LogStatus;
import com.gogoyang.rpgapi.meta.email.entity.Email;
import com.gogoyang.rpgapi.meta.email.service.IEmailService;
import com.gogoyang.rpgapi.meta.phone.entity.Phone;
import com.gogoyang.rpgapi.meta.phone.service.IPhoneService;
import com.gogoyang.rpgapi.meta.realname.entity.RealName;
import com.gogoyang.rpgapi.meta.realname.service.IRealNameService;
import com.gogoyang.rpgapi.meta.user.entity.UserInfo;
import com.gogoyang.rpgapi.meta.user.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final ICommonBusinessService iCommonBusinessService;

    @Autowired
    public ProfileBusinessService(IEmailService iEmailService,
                                  IUserService iUserService,
                                  IPhoneService iPhoneService,
                                  IRealNameService iRealNameService,
                                  ICommonBusinessService iCommonBusinessService) {
        this.iEmailService = iEmailService;
        this.iUserService = iUserService;
        this.iPhoneService = iPhoneService;
        this.iRealNameService = iRealNameService;
        this.iCommonBusinessService = iCommonBusinessService;
    }

    @Override
    public Map getUserInfo(Map in) throws Exception {
        String token = in.get("token").toString();
        UserInfo user = iUserService.getUserByToken(token);
        Map out = new HashMap();
        out.put("user", user);
        return out;
    }

    @Override
    public Map listPhoneOfUser(Map in) throws Exception {
        String token = in.get("token").toString();
        UserInfo user = iCommonBusinessService.getUserByToken(token);

        ArrayList<Phone> phones = iPhoneService.listPhoneByUserId(user.getUserId());

        Map out = new HashMap();
        out.put("phones", phones);
        return out;
    }

    @Override
    public Map listEmailOfUser(Map in) throws Exception {
        String token = in.get("token").toString();
        UserInfo user = iCommonBusinessService.getUserByToken(token);

        ArrayList<Email> emails=iEmailService.listEmailByUserId(user.getUserId());

        Map out = new HashMap();
        out.put("emails", emails);
        return out;
    }

    @Override
    public Map getUserProfile(Map in) throws Exception {
        String token=in.get("token").toString();

        UserInfo user=iCommonBusinessService.getUserByToken(token);

        RealName realName=iRealNameService.getRealNameByUserId(user.getUserId());

        Map out=new HashMap();
        out.put("realname", realName);
        return out;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRealName(Map in) throws Exception {
        String token=in.get("token").toString();
        String realname=in.get("realname").toString();
        String idcardNo=(String)in.get("idcardNo");
        String sex=in.get("sex").toString();

        UserInfo user=iCommonBusinessService.getUserByToken(token);

        RealName realName=iRealNameService.getRealNameByUserId(user.getUserId());
        if(realName==null){
            //insert
            realName=new RealName();
            realName.setCreatedTime(new Date());
            realName.setUserId(user.getUserId());
            realName.setRealName(realname);
            realName.setIdcardNo(idcardNo);
            if(sex.equals(GenderType.Male.toString())){
                realName.setSex(GenderType.Male.toString());
            }else{
                if(sex.equals(GenderType.Female.toString())){
                    realName.setSex(GenderType.Female.toString());
                }
            }
            //新增时的状态只能是等待审核
            realName.setStatus(LogStatus.PENDING.toString());
            iRealNameService.insert(realName);
        }else{
            /**
             * update
             * 已经认证的实名信息不能修改，所以只有PENDING状态的可以修改
             */
            if(!realName.getStatus().equals(LogStatus.PENDING.toString())){
                throw new Exception("30004");
            }
            realName.setRealName(realname);
            realName.setIdcardNo(idcardNo);
            if(sex.equals(GenderType.Male.toString())) {
                realName.setSex(sex);
            }else{
                if(sex.equals(GenderType.Female.toString())){
                    realName.setSex(sex);
                }
            }
            iRealNameService.update(realName);
        }

        /**
         * update user
         * 修改userInfo表的real_name
         */
        UserInfo userInfoEdit=new UserInfo();
        userInfoEdit.setUserId(user.getUserId());
        userInfoEdit.setRealName(realname);
        iUserService.updateUserInfo(userInfoEdit);
    }

    /**
     * 用户申请任务时填写的联系信息
     * @param in
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveUserContactInfo(Map in) throws Exception {
        /**
         * 通常来说，用户注册时不会去填写太多信息
         * 在中国市场来说，用户一般选择手机号码注册，且注册时已经进行了短信验证。这个在中国市场已经形成了大家比较能接受的流程。
         * 用户查看到可以承接的任务，点击申请后，会弹出一个页面，让用户填写申请说明，以及真实姓名，电话和email
         * 此时的逻辑是，如果用户已经进行实名认证，则此时姓名是不能更改的。但没有认证时，可随意更改。
         * 同样，电话和email无论是否认证，都可以更改，因为用户可能希望留下另一个联系电话和email
         * 此时，只要用户填写的email或者电话和注册时不一致，则重新创建一个email或电话记录，绑定到该用户。
         * 同时，jobApply申请表里需要增加phone，或者email的记录，以说明该用户使用哪个电话或email来进行该任务的联系
         *
         */
        String token=in.get("token").toString();
        String phoneStr=(String)in.get("phone");
        String emailStr=(String)in.get("email");
        String realNameStr=(String)in.get("realName");

        UserInfo userInfo=iCommonBusinessService.getUserByToken(token);

        if(phoneStr!=null){
            Phone phone=iPhoneService.getPhoneByPhone(phoneStr);
            if(phone==null){
                phone=new Phone();
                phone.setPhoneId(GogoTools.UUID());
                phone.setPhone(phoneStr);
                phone.setCreatedTime(new Date());
                phone.setCreatedUserId(userInfo.getUserId());
                phone.setUserId(userInfo.getUserId());
                iPhoneService.insert(phone);
            }
        }

        if(emailStr!=null){
            Email email=iEmailService.getEmailByEmail(emailStr);
            if(emailStr==null){
                email=new Email();
                email.setEmailId(GogoTools.UUID());
                email.setEmail(emailStr);
                email.setUserId(userInfo.getUserId());
                email.setCreatedTime(new Date());
                email.setCreatedUserId(userInfo.getUserId());
                iEmailService.insert(email);
            }
        }

        if(realNameStr!=null){
            RealName realName=iRealNameService.getRealNameByUserId(userInfo.getUserId());
            if(realName==null){
                //当前用户没有实名，先创建一个再说
                realName=new RealName();
                realName.setRealName(realNameStr);
                realName.setUserId(userInfo.getUserId());
                realName.setVerify(LogStatus.PENDING.toString());
                realName.setStatus(GogoStatus.ACTIVE.toString());
                iRealNameService.insert(realName);
            }else{
                //有实名，检查是否已经验证
                if(realName.getVerify().equals(GogoStatus.VERIFIED.toString())){
                    //已认证，不能修改
                    throw new Exception("30010");
                }else{
                    //修改实名
                    RealName realNameEdit=new RealName();
                    realNameEdit.setUserId(userInfo.getUserId());
                    realNameEdit.setRealName(realNameStr);
                    iRealNameService.update(realNameEdit);
                }
            }
        }
    }
}
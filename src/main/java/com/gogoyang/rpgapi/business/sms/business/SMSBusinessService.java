package com.gogoyang.rpgapi.business.sms.business;

import com.gogoyang.rpgapi.framework.common.IRPGFunction;
import com.gogoyang.rpgapi.framework.constant.LogStatus;
import com.gogoyang.rpgapi.meta.sms.entity.SMSLog;
import com.gogoyang.rpgapi.meta.sms.service.ISMSLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;
import java.util.Random;

@Service
public class SMSBusinessService implements ISMSBusinessService {
    private final IRPGFunction irpgFunction;
    private final ISMSLogService ismsLogService;

    public SMSBusinessService(IRPGFunction irpgFunction,
                              ISMSLogService ismsLogService) {
        this.irpgFunction = irpgFunction;
        this.ismsLogService = ismsLogService;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void getPhoneVerifyCode(Map in) throws Exception {
        String phone = (String) in.get("phone");
        if (phone == null) {
            throw new Exception("10105");
        }
        /**
         * 生成验证码
         */
        String codeStr = String.valueOf(new Random().nextInt(899999) + 100000);
        /**
         * 发送短信
         */
        irpgFunction.sendMSM(phone, codeStr);

        /**
         * 发送成功，把验证码保存到数据库
         */

        SMSLog smsLog = new SMSLog();
        smsLog.setCode(codeStr);
        smsLog.setCreateTime(new Date());
        smsLog.setPhone(phone);
        smsLog.setStatus(LogStatus.WAITING.toString());
        ismsLogService.createSMSLog(smsLog);
    }
}

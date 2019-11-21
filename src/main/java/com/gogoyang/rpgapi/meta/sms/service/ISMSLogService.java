package com.gogoyang.rpgapi.meta.sms.service;

import com.gogoyang.rpgapi.meta.sms.entity.SMSLog;


public interface ISMSLogService {
    void createSMSLog(SMSLog smsLog) throws Exception;

    SMSLog getSMSLog(String phone, String code) throws Exception;

    void updateSMSLog(SMSLog smsLog) throws Exception;
}

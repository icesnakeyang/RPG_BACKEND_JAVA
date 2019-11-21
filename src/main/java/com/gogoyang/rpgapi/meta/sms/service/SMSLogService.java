package com.gogoyang.rpgapi.meta.sms.service;

import com.gogoyang.rpgapi.meta.sms.dao.SMSLogDao;
import com.gogoyang.rpgapi.meta.sms.entity.SMSLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SMSLogService implements ISMSLogService {
    private final SMSLogDao smsLogDao;

    public SMSLogService(SMSLogDao smsLogDao) {
        this.smsLogDao = smsLogDao;
    }

    @Override
    public void createSMSLog(SMSLog smsLog) throws Exception {
        smsLogDao.insertSmsLog(smsLog);
    }

    @Override
    public SMSLog getSMSLog(String phone, String code) throws Exception {
        Map qIn = new HashMap();
        qIn.put("phone", phone);
        qIn.put("code", code);
        SMSLog smsLog = smsLogDao.getSmsLog(qIn);
        return smsLog;
    }

    @Override
    public void updateSMSLog(SMSLog smsLog) throws Exception {
        smsLogDao.updateSMSLog(smsLog);
    }
}

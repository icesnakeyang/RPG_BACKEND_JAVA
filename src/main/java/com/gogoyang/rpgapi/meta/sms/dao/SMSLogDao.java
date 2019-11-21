package com.gogoyang.rpgapi.meta.sms.dao;

import com.gogoyang.rpgapi.meta.sms.entity.SMSLog;
import org.apache.ibatis.annotations.Mapper;

import java.util.Map;

@Mapper
public interface SMSLogDao {
    void insertSmsLog(SMSLog smsLog);

    SMSLog getSmsLog(Map qIn);

    void updateSMSLog(SMSLog smsLog);
}

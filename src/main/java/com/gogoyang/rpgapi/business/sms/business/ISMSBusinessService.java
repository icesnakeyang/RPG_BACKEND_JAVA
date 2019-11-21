package com.gogoyang.rpgapi.business.sms.business;

import java.util.Map;

public interface ISMSBusinessService {
    void getPhoneVerifyCode(Map in) throws Exception;
}

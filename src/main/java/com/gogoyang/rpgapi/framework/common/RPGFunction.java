package com.gogoyang.rpgapi.framework.common;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.fasterxml.jackson.databind.JsonNode;
import com.gogoyang.rpgapi.framework.constant.LogStatus;
import com.gogoyang.rpgapi.meta.sms.entity.SMSLog;
import com.gogoyang.rpgapi.meta.sms.service.ISMSLogService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

import java.security.MessageDigest;
import java.util.*;

@Service
public class RPGFunction implements IRPGFunction {
    private final ISMSLogService ismsLogService;

    private Logger logger = LoggerFactory.getLogger(getClass());

    public RPGFunction(ISMSLogService ismsLogService) {
        this.ismsLogService = ismsLogService;
    }

    /**
     * 把传入的字符串进行MD5加密后，返回生成的MD5码
     *
     * @param string
     * @return
     * @throws Exception
     */
    @Override
    public String encoderByMd5(String string) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        BASE64Encoder base64Encoder = new BASE64Encoder();
        String newString = base64Encoder.encode(md5.digest(string.getBytes("utf-8")));

        return newString;
    }

    /**
     * 发送短信验证码
     *
     * @param phone
     * @throws Exception
     */
    @Override
    public void sendMSM(String phone, String codeStr) throws Exception {
        DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "0O9R0XlXcfYcPUwB", "Vhx4wv8LUrpkQkeLQ0BYKMPg9KV950");
        IAcsClient client = new DefaultAcsClient(profile);

        CommonRequest request = new CommonRequest();
        request.setMethod(MethodType.POST);
        request.setDomain("dysmsapi.aliyuncs.com");
        request.setVersion("2017-05-25");
        request.setAction("SendSms");
        request.putQueryParameter("RegionId", "cn-hangzhou");
        request.putQueryParameter("PhoneNumbers", phone);
        request.putQueryParameter("SignName", "GOGORPG");
        request.putQueryParameter("TemplateCode", "SMS_177544037");
        request.putQueryParameter("TemplateParam", "{\"code\":\"" + codeStr + "\"}");
        CommonResponse response = client.getCommonResponse(request);
        String result = response.getData().substring(response.getData().length() - 4, response.getData().length() - 2);
        if (!result.equals("OK")) {
            throw new Exception("10106");
        }
        logger.info(response.getData());
    }

    @Override
    public void verifyMSMCode(String phone, String code) throws Exception {
        SMSLog smsLog = ismsLogService.getSMSLog(phone, code);
        if (smsLog == null) {
            throw new Exception("10108");
        }

        if (!smsLog.getStatus().equals(LogStatus.WAITING.toString())) {
            throw new Exception("10109");
        }

        /**
         * 计算当前时间是否已经超过15分钟
         */
        Date now = new Date();
        long currentTime = now.getTime();
        long theTime = smsLog.getCreateTime().getTime();

        long diff = (currentTime - theTime) / 1000 / 60;

        if (diff > 15) {
            throw new Exception("10109");
        }
    }

    public String convertMapToString(HashMap map) throws Exception {
        Iterator iter = map.entrySet().iterator();
        String outStr = "";
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            outStr += key.toString() + ":";
            Object val = entry.getValue();
            outStr += val.toString() + "/";
        }
        return outStr;
    }

    public UUID UUID() throws Exception {
        UUID uuid = UUID.randomUUID();
        return uuid;
    }
}

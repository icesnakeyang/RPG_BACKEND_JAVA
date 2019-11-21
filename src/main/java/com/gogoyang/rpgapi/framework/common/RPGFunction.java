package com.gogoyang.rpgapi.framework.common;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

import java.security.MessageDigest;
import java.util.Map;
import java.util.Random;

@Service
public class RPGFunction implements IRPGFunction {

    private Logger logger = LoggerFactory.getLogger(getClass());

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
     * @param phone
     * @throws Exception
     */
    @Override
    public void sendMSM(String phone) throws Exception {
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
        /**
         * 生成验证码
         */
        String codeStr = String.valueOf(new Random().nextInt(899999) + 100000);
        request.putQueryParameter("TemplateParam", "{\"code\":\"" + codeStr + "\"}");
        CommonResponse response = client.getCommonResponse(request);
        String result=response.getData().substring(response.getData().length()-4,response.getData().length()-2);
        if(!result.equals("OK")){
            throw new Exception("10106");
        }
        logger.info(response.getData());
    }
}

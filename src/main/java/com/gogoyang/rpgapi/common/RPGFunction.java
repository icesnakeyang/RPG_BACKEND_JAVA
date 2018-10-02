package com.gogoyang.rpgapi.common;

import org.springframework.stereotype.Service;
import sun.misc.BASE64Encoder;

import java.security.MessageDigest;

@Service
public class RPGFunction implements IRPGFunction {

    /**
     * 把传入的字符串进行MD5加密后，返回生成的MD5码
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
}

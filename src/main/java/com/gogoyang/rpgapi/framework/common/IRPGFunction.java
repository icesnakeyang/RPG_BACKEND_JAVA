package com.gogoyang.rpgapi.framework.common;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public interface IRPGFunction {

    String encoderByMd5(String string) throws Exception;

    void sendMSM(String phone, String codeStr) throws Exception;

    void verifyMSMCode(String phone, String code) throws Exception;

    String convertMapToString(HashMap map) throws Exception;

    UUID UUID() throws Exception;

    boolean checkEmail(String email) throws Exception;

    /**
     * 把base64图片数据转成图片文件
     * @param imgStr
     * @return
     * @throws Exception
     */
    Map GenerateImage(String imgStr, Integer fileIndex) throws Exception;

    Map processRichTextPics(String detail) throws Exception;

    void deleteOSSFile(String fileName) throws Exception;
}

package com.gogoyang.rpgapi.framework.common;

import java.util.HashMap;
import java.util.UUID;

public interface IRPGFunction {

    String encoderByMd5(String string) throws Exception;

    void sendMSM(String phone, String codeStr) throws Exception;

    void verifyMSMCode(String phone, String code) throws Exception;

    String convertMapToString(HashMap map) throws Exception;

    UUID UUID() throws Exception;
}

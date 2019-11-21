package com.gogoyang.rpgapi.framework.common;

import java.util.Map;

public interface IRPGFunction {

    String encoderByMd5(String string) throws Exception;

    void sendMSM(String phone) throws Exception;

}

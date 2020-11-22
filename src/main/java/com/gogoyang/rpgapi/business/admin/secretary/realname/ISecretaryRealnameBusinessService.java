package com.gogoyang.rpgapi.business.admin.secretary.realname;

import java.util.Map;

public interface ISecretaryRealnameBusinessService {
    /**
     * 秘书读取用户的实名认证申请列表
     * @param in
     * @return
     * @throws Exception
     */
    Map listRealnamePending(Map in) throws Exception;

    Map getRealnamePending(Map in) throws Exception;

    void agreeRealname(Map in) throws Exception;

    void rejectRealname(Map in) throws Exception;
}

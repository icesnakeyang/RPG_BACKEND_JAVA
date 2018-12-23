package com.gogoyang.rpgapi.business.admin.secretary.user.service;

import java.util.Map;

public interface ISecretaryUserBusinessService {
    /**
     * read one users apply history
     * @param in
     * @return
     * @throws Exception
     */
    Map listApplyHistory(Map in) throws Exception;
}

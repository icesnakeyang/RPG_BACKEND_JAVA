package com.gogoyang.rpgapi.business.admin.secretary.match.service;

import java.util.Map;

public interface ISecretaryMatchBusinessService {
    Map loadJobToMatch(Map in) throws Exception;

    void matchJobToUser(Map in) throws Exception;

    Map loadUserApplyJob(Map in) throws Exception;

}

package com.gogoyang.rpgapi.business.admin.secretary.match.service;

import com.gogoyang.rpgapi.meta.user.userInfo.entity.UserInfo;

import java.util.ArrayList;
import java.util.Map;

public interface ISecretaryMatchBusinessService {
    Map loadJobToMatch(Map in) throws Exception;

    void matchJobToUser(Map in) throws Exception;

    ArrayList<UserInfo> loadApplyUserByJobId(Integer jobId) throws Exception;

}

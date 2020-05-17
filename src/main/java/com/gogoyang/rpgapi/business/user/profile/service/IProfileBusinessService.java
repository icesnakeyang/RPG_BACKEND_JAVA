package com.gogoyang.rpgapi.business.user.profile.service;


import java.util.Map;

public interface IProfileBusinessService {
    Map getUserInfo(Map in) throws Exception;

    Map listPhoneOfUser(Map in) throws Exception;

    Map listEmailOfUser(Map in) throws Exception;

    Map getUserProfile(Map in) throws Exception;

    void saveRealName(Map in) throws Exception;

    /**
     * 用户申请任务时填写的联系信息
     * @param in
     * @throws Exception
     */
    void saveUserContactInfo(Map in) throws Exception;
}

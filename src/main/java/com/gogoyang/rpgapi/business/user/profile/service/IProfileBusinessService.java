package com.gogoyang.rpgapi.business.user.profile.service;


import java.util.Map;

public interface IProfileBusinessService {
    void saveUserContactInfo(Map in) throws Exception;

    Map getUserInfo(Map in) throws Exception;

    Map listPhoneOfUser(Map in) throws Exception;

    Map listEmailOfUser(Map in) throws Exception;
}

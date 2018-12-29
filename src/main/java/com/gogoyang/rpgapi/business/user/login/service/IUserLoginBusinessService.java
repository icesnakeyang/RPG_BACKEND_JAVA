package com.gogoyang.rpgapi.business.user.login.service;

import java.util.Map;

public interface IUserLoginBusinessService {
    Map loginByEmail(Map in) throws Exception;
}
